package io.odiszapc.jgrep.fs.local;

import io.odiszapc.jgrep.fs.ObjectsIterable;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.stream.StreamSupport;

/**
 * {@link ObjectsIterable} collection implementation for directory in local file system
 * <p>
 * Used to iterate over the specified directory to the given directory to iterate over its files
 * <p>
 * Once created opens a {@link java.util.stream.Stream}
 */
public class FileSystemObjects implements ObjectsIterable<FileSystemDescriptor>, Closeable {
    private final DirectoryStream<Path> directoryStream;

    /**
     * Construct iterator for given directory
     *
     * @param directory local directory {@link Path}
     */
    public FileSystemObjects(Path directory) throws IOException {
        directoryStream = Files.newDirectoryStream(directory);
    }

    /**
     * Returns iterator for given directory
     *
     * @return instance of {@link Iterator< FileSystemDescriptor >}
     */
    @Override
    public Iterator<FileSystemDescriptor> it() {
        return StreamSupport.stream(directoryStream.spliterator(), false).map(FileSystemDescriptor::of).iterator();
    }

    /**
     * Close directory {@link java.util.stream.Stream}
     */
    @Override
    public void close() throws IOException {
        directoryStream.close();
    }
}
