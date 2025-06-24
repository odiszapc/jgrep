package io.odiszapc.jgrep.stats;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Thread-safe implementation of statistics snapshot
 */
public class Statistics {
    private static final AtomicLong objectsProcessed = new AtomicLong(0);
    private static final AtomicLong bytesProcessed = new AtomicLong(0);
    private static final AtomicLong linesProcessed = new AtomicLong(0);
    private static final AtomicLong linesMatched = new AtomicLong(0);

    public void incFilesProcessed() {
        objectsProcessed.incrementAndGet();
    }

    public void addLinesProcessed(int lines) {
        linesProcessed.addAndGet(lines);
    }

    public void addLinesMatched(int lines) {
        linesMatched.addAndGet(lines);
    }

    public void addBytesProcessed(long bytes) {
        bytesProcessed.addAndGet(bytes);
    }

    public long filesProcessed() {
        return objectsProcessed.get();
    }

    public long bytesProcessed() {
        return bytesProcessed.get();
    }

    public long linesProcessed() {
        return linesProcessed.get();
    }

    public long linesMatched() {
        return linesMatched.get();
    }
}
