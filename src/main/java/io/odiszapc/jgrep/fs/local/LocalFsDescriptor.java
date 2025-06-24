package io.odiszapc.jgrep.fs.local;

import io.odiszapc.jgrep.fs.ObjectData;
import io.odiszapc.jgrep.fs.ObjectDescriptor;
import io.odiszapc.jgrep.fs.ObjectsIterable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * {@link ObjectDescriptor} implementation for local file system
 */
public class LocalFsDescriptor implements ObjectDescriptor {
    private final Path path;

    public static LocalFsDescriptor of(Path path) {
        return new LocalFsDescriptor(path);
    }

    public static LocalFsDescriptor of(String path) {
        return new LocalFsDescriptor(Path.of(path));
    }

    @Override
    public String objectName() {
        return path.getFileName().toString();
    }

    @Override
    public String fullPath() {
        return path.toAbsolutePath().toString();
    }

    @Override
    public ObjectsIterable<? extends ObjectDescriptor> children() throws IOException {
        return new LocalFsObjects(path);
    }

    public LocalFsDescriptor(String localPath) {
        path = Path.of(localPath);
    }

    public LocalFsDescriptor(Path localPath) {
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
    public ObjectData toObject() {
        return new LocalFsObject(path);
    }
}
