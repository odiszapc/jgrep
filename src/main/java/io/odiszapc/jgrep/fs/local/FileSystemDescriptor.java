package io.odiszapc.jgrep.fs.local;

import io.odiszapc.jgrep.fs.ObjectDescriptor;
import io.odiszapc.jgrep.fs.ObjectsIterable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * {@link ObjectDescriptor} implementation for local file system
 */
public class FileSystemDescriptor implements ObjectDescriptor {
    private final Path path;

    public static FileSystemDescriptor of(Path path) {
        return new FileSystemDescriptor(path);
    }

    public static FileSystemDescriptor of(FileSystem fs, String path) {
        return new FileSystemDescriptor(fs.getPath(path));
    }

    @Override
    public String name() {
        return path.getFileName().toString();
    }

    @Override
    public long size() throws IOException {
        return Files.size(path);
    }

    @Override
    public ObjectsIterable<? extends ObjectDescriptor> children() throws IOException {
        return new FileSystemObjects(path);
    }

    public FileSystemDescriptor(Path localPath) {
        path = localPath;
    }

    public Path getPath() {
        return path;
    }

    @Override
    public boolean isContainer() {
        return Files.isDirectory(path);
    }

    @Override
    public InputStream inputStream() throws IOException {
        return Files.newInputStream(path, StandardOpenOption.READ);
    }
}
