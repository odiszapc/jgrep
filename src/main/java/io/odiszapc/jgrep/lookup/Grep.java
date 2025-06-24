package io.odiszapc.jgrep.lookup;

import io.odiszapc.jgrep.format.LinuxGrepLineLineFormatter;
import io.odiszapc.jgrep.fs.ObjectDescriptor;
import io.odiszapc.jgrep.fs.ObjectStore;
import io.odiszapc.jgrep.matcher.Matcher;
import io.odiszapc.jgrep.matcher.RegexMatcher;
import io.odiszapc.jgrep.matcher.SimpleMatcher;
import io.odiszapc.jgrep.output.StdoutPrinter;
import io.odiszapc.jgrep.stats.Statistics;
import io.odiszapc.jgrep.stats.StatisticsFormatter;

import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Grep {
    private final ExecutorService pool;
    private final ObjectDescriptor rootContainer;
    private final CountDownLatch shutdownLatch = new CountDownLatch(1);
    private final Statistics statistics;
    private Future<?> storeTraversingFut;
    private final Matcher matcher;

    private static final AtomicInteger filesToProcessCounter = new AtomicInteger(0);
    private final StdoutPrinter output;

    public static void plainSearch(ObjectStore store, String rootDir, String pattern, int nThreads) throws ExecutionException, InterruptedException {
        run(store, rootDir, nThreads, new SimpleMatcher(pattern));
    }

    public static void regexSearch(ObjectStore store, String rootDir, String pattern, int nThreads) throws ExecutionException, InterruptedException {
        run(store, rootDir, nThreads, new RegexMatcher(pattern));
    }

    private static void run(ObjectStore store, String rootDir, int nThreads, Matcher matcher) throws ExecutionException, InterruptedException {
        buildGrep(store, rootDir, nThreads, matcher)
                .startAsync()
                .waitForFinish();
    }

    private static Grep buildGrep(ObjectStore store, String rootDir, int nThreads, Matcher matcher) {
        return new Grep(store.objectDescriptor(rootDir), nThreads, matcher);
    }

    public Grep(ObjectDescriptor rootContainer, int nThreads, Matcher matcher) {
        this.rootContainer = rootContainer;
        this.matcher = matcher;
        this.output = new StdoutPrinter(new LinuxGrepLineLineFormatter());
        this.pool = Executors.newFixedThreadPool(nThreads);
        this.statistics = new Statistics();
    }

    private Void onObjectProcessed(Integer linesProcessed, Integer linesMatched) {
        statistics.addLinesMatched(linesMatched);
        statistics.addLinesProcessed(linesProcessed);
        return null;
    }

    private void onObjectFound(ObjectDescriptor objectPath) {
        // Encounter a data object
        filesToProcessCounter.incrementAndGet();

        statistics.incFilesProcessed();

        pool.submit(() -> {
            try {
                new TextSearch(objectPath, matcher, output, this::onObjectProcessed).run();
                statistics.addBytesProcessed(objectPath.toObject().size());
            } catch (IOException e) {
                // TODO: do not ignore problematic files
                throw new RuntimeException(e);
            }
            filesToProcessCounter.decrementAndGet();

            if (storeTraversingFut.isDone()) {
                // Traversing was finished, wait for the rest of file content search tasks to complete
                if (filesToProcessCounter.get() == 0) {
                    shutdownLatch.countDown();
                }
            }
        });
    }

    public Grep startAsync() {
        storeTraversingFut = pool.submit(() -> {
            DirTraverser.start(rootContainer, this::onObjectFound);
        });

        return this;
    }

    public void waitForFinish() throws ExecutionException, InterruptedException {
        // Wait for directory traversing to complete
        storeTraversingFut.get();

        // Wait for the rest of the objects being processed, it's safe as traversing is finished
        shutdownLatch.await();

        // Stop the pool
        pool.shutdown();

        // Optionally; display stats
        outputStatistics();
    }

    private void outputStatistics() {
        StatisticsFormatter.print(statistics);
    }
}
