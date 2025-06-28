package io.odiszapc.jgrep.lookup;

import com.google.common.collect.ImmutableList;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import io.odiszapc.jgrep.fs.local.FileSystemStore;
import io.odiszapc.jgrep.match.ContainsMatcher;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GrepTest {

    /**
     * 1. Build in-memory filesystem
     * 2. Write file
     * 3. Grep over virtual fs
     */
    @Test
    void grepTest() throws ExecutionException, InterruptedException, IOException {
        final MockOutputCollector output = new MockOutputCollector();
        final FileSystem fs = Jimfs.newFileSystem(Configuration.unix());

        final Path directory = fs.getPath("/foo");
        Files.createDirectory(directory);

        final Path file = directory.resolve("hello.txt"); // Write to /foo/hello.txt
        Files.write(file, ImmutableList.of("hello world"), StandardCharsets.UTF_8);

        Grep.create(new FileSystemStore(fs), "/foo", 4, new ContainsMatcher("hello"), output)
                .startAsync()
                .waitForFinish();

        assertTrue(output.contains("hello.txt:1:hello world"));
    }
}
