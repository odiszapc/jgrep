package io.odiszapc.jgrep.lookup;

import com.google.common.collect.ImmutableList;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import io.odiszapc.jgrep.JGrep;
import io.odiszapc.jgrep.fs.local.FileSystemStore;
import io.odiszapc.jgrep.match.ContainsMatcher;
import io.odiszapc.jgrep.stats.Statistics;
import io.odiszapc.jgrep.stats.StatisticsPrinter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JGrepTest {
    private static class NopeStatPrinter implements StatisticsPrinter {

        public Statistics stats;

        @Override
        public void print(Statistics statistics) {
            stats = statistics;
        }
    }

    /**
     * 1. Build in-memory filesystem
     * 2. Write file
     * 3. Grep over virtual fs
     */
    @Test
    void grepTest() throws IOException {
        final MockOutputCollector output = new MockOutputCollector();
        final NopeStatPrinter statCollector = new NopeStatPrinter();
        final FileSystem fs = Jimfs.newFileSystem(Configuration.unix());
        final ContainsMatcher matcher = new ContainsMatcher("hello");

        // Prepare dir/file hierarchy
        final Path directory = fs.getPath("/foo");
        Files.createDirectory(directory);
        final Path file = directory.resolve("hello.txt");
        Files.write(file, ImmutableList.of("hello world"), StandardCharsets.UTF_8);

        // Run grep
        JGrep.create(new FileSystemStore(fs), "/foo", 4, matcher, output, statCollector)
                .startAsync()
                .waitForFinish();

        assertTrue(output.contains("hello.txt:1:hello world"));

        assertEquals(1, statCollector.stats.linesMatched());
        assertEquals(Files.size(file), statCollector.stats.bytesProcessed());
        assertEquals(1, statCollector.stats.linesProcessed());
        assertEquals(1, statCollector.stats.filesProcessed());
    }
}
