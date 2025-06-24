package io.odiszapc.jgrep.fs.local;

import io.odiszapc.jgrep.fs.ObjectsIterable;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.stream.StreamSupport;

public class LocalFsObjects implements ObjectsIterable<LocalFsDescriptor>, Closeable {

    private final DirectoryStream<Path> dirStream;

    public LocalFsObjects(LocalFsDescriptor directory) throws IOException {
        this(directory.getPath());
    }

    public LocalFsObjects(Path directory) throws IOException {
        dirStream = Files.newDirectoryStream(directory);
    }

    @Override
    public Iterator<LocalFsDescriptor> it() {
        return StreamSupport.stream(dirStream.spliterator(), false)
                .map(LocalFsDescriptor::of).iterator();
    }

    @Override
    public void close() throws IOException {
        dirStream.close();
    }
}
