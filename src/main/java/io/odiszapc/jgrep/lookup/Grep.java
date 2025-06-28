package io.odiszapc.jgrep.lookup;

import io.odiszapc.jgrep.fs.ObjectDescriptor;
import io.odiszapc.jgrep.fs.ObjectStore;
import io.odiszapc.jgrep.match.ContainsMatcher;
import io.odiszapc.jgrep.match.IgnoreCaseMatcher;
import io.odiszapc.jgrep.match.Matcher;
import io.odiszapc.jgrep.match.RegexMatcher;
import io.odiszapc.jgrep.output.LinuxGrepLineLineFormatter;
import io.odiszapc.jgrep.output.OutputPrinter;
import io.odiszapc.jgrep.output.StdoutPrinter;
import io.odiszapc.jgrep.stats.Statistics;
import io.odiszapc.jgrep.stats.StatisticsPrinter;
import io.odiszapc.jgrep.stats.StdoutStatisticPrinter;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Multi-threaded implementation of grep util
 * <p>
 * Starts recursive directory traversing on a separate thread, and do it in a one thread
 * <p>
 * Each file descriptor is sent over the same thread pool and processed concurrently
 * <p>
 * Once traversing is done we wait for the rest of the file parsing tasks to complete.
 */
public class Grep {
    /**
     * Common thread pool that is used for
     * - Traversing directory hierarchy
     * - Parse each file in a concurrent manner
     */
    private final ExecutorService pool;

    /**
     * Parent container to start the process from
     */
    private final ObjectDescriptor containerPath;

    /**
     * Latch that used as a trigger for awaiting the whole process to finish.
     * <p>
     * Is activated when we detected traversing over directory hierarchy is completed
     */
    private final CountDownLatch shutdownLatch = new CountDownLatch(1);

    /**
     * Util to collect statistics about how much is processed/parsed/matched
     */
    private final Statistics statistics;

    /**
     * Future that completed when we traverse all the directory hierarchy
     */
    private Future<?> storeTraversingFut;

    /**
     * Strategy for searching inside the file:
     * - {@link ContainsMatcher}
     * - {@link IgnoreCaseMatcher}
     * - {@link RegexMatcher}
     */
    private final Matcher matcher;

    /**
     * Thread-safe counter representing number of files to be processed
     * Incremented on traversing
     * Decremented when file was parsed
     */
    private final AtomicInteger filesToProcessCounter = new AtomicInteger(0);

    /**
     * Grep output sink (stdout by default)
     */
    private final OutputPrinter output;

    StatisticsPrinter statisticsPrinter;

    private final static OutputPrinter stdout = new StdoutPrinter(new LinuxGrepLineLineFormatter());

    private final static StatisticsPrinter statStdout = new StdoutStatisticPrinter();


    /**
     * Build {@link Grep} instance and start search with a {@link ContainsMatcher} strategy
     *
     * @param store         File system abstraction implementation
     * @param containerPath directory path
     * @param nThreads      Number of thread
     */
    public static void plainSearch(ObjectStore store, String containerPath, String pattern, int nThreads) throws ExecutionException, InterruptedException {
        run(store, containerPath, nThreads, new ContainsMatcher(pattern), stdout, statStdout);
    }

    /**
     * Build {@link Grep} instance and start search with a {@link IgnoreCaseMatcher} strategy
     *
     * @param store         File system abstraction implementation
     * @param containerPath directory path
     * @param nThreads      Number of thread
     */
    public static void ignoreCaseSearch(ObjectStore store, String containerPath, String pattern, int nThreads) throws ExecutionException, InterruptedException {
        run(store, containerPath, nThreads, new ContainsMatcher(pattern), stdout, statStdout);
    }

    /**
     * Build {@link Grep} instance and start search with a {@link RegexMatcher} strategy
     *
     * @param store         File system abstraction implementation
     * @param containerPath directory path
     * @param nThreads      Number of thread
     */
    public static void regexSearch(ObjectStore store, String containerPath, String pattern, int nThreads) throws ExecutionException, InterruptedException {
        run(store, containerPath, nThreads, new RegexMatcher(pattern), stdout, statStdout);
    }

    /**
     * Build {@link Grep} instance and start search
     *
     * @param store         File system abstraction implementation
     * @param containerPath directory path
     * @param nThreads      Number of thread
     * @param matcher       Text search strategy
     */
    private static void run(ObjectStore store,
                            String containerPath,
                            int nThreads,
                            Matcher matcher,
                            OutputPrinter output,
                            StatisticsPrinter statisticsPrinter) throws ExecutionException, InterruptedException {
        create(store, containerPath, nThreads, matcher, output, statisticsPrinter)
                .startAsync()
                .waitForFinish();
    }

    /**
     * Build {@link Grep} instance
     *
     * @param store         File system abstraction implementation
     * @param containerPath directory path
     * @param nThreads      Number of thread
     * @param matcher       Text search strategy
     * @param output        Output handler
     * @return {@link Grep} instance
     */
    public static Grep create(ObjectStore store,
                              String containerPath,
                              int nThreads,
                              Matcher matcher,
                              OutputPrinter output,
                              StatisticsPrinter statisticsPrinter) {
        return new Grep(store.objectDescriptor(containerPath), nThreads, matcher, output, statisticsPrinter);
    }

    /**
     * Build {@link Grep} instance
     *
     * @param containerPath directory path
     * @param nThreads      Number of thread
     * @param matcher       Text search strategy
     */
    public Grep(ObjectDescriptor containerPath,
                int nThreads,
                Matcher matcher,
                OutputPrinter output,
                StatisticsPrinter statisticsPrinter) {
        this.containerPath = containerPath;
        this.matcher = matcher;
        this.output = output;
        this.statisticsPrinter = statisticsPrinter;

        // Create a fixed-size thread pool to handle parallel file processing
        this.pool = Executors.newFixedThreadPool(nThreads);
        this.statistics = new Statistics();
    }

    private Void onObjectProcessed(Integer linesProcessed, Integer linesMatched) {
        statistics.addLinesMatched(linesMatched);
        statistics.addLinesProcessed(linesProcessed);
        return null;
    }

    private void onObjectFound(ObjectDescriptor path) {
        // Encounter a data object
        // Atomically increment the file counter when a new file is discovered
        // We will use it as a trigger to await for the process to finish
        filesToProcessCounter.incrementAndGet();

        statistics.incFilesProcessed();

        // Submit a new task to the thread pool to process each file concurrently
        pool.submit(() -> {
            try {
                // Create and run a TextSearch instance for line matching within this file
                new TextSearch(path, matcher, output, this::onObjectProcessed).run();

                // Accumulate the size of the processed file to the statistics
                statistics.addBytesProcessed(path.size());
            } catch (Throwable e) {
                // TODO: Do not ignore failed files
            }
            filesToProcessCounter.decrementAndGet();

            // Traversing was finished, wait for the rest of file content search tasks to complete
            if (storeTraversingFut.isDone() && filesToProcessCounter.get() == 0) {
                shutdownLatch.countDown();
            }
        });
    }

    public Grep startAsync() {
        storeTraversingFut = pool.submit(() -> {
            ObjectTreeWalker.run(containerPath, this::onObjectFound);
        });

        return this;
    }

    public void waitForFinish() throws ExecutionException, InterruptedException {
        // Wait for directory traversing to complete
        storeTraversingFut.get();

        // Wait for the rest of the objects being processed, it's safe as traversing is finished
        shutdownLatch.await();

        // Stop the pool just in case (not necessary step as all tasks were finished on a previous step
        pool.shutdown();

        // Optionally, display stats
        statisticsPrinter.print(statistics);
    }
}
