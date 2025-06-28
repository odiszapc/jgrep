package io.odiszapc.jgrep.fs.local;

import io.odiszapc.jgrep.fs.ObjectDescriptor;
import io.odiszapc.jgrep.fs.ObjectStore;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;

/**
 * Object store implementation for Local (Windows, *nix) file system
 */
public class FileSystemStore implements ObjectStore {

    private static final FileSystemStore instance = new FileSystemStore(FileSystems.getDefault());
    private FileSystem fs;

    public FileSystemStore(FileSystem fs) {
        this.fs = fs;
    }

    public static FileSystemStore defaultFS() {
        return instance;
    }

    /**
     * Return {@link ObjectDescriptor} wrapper for the specified object path
     *
     * @param objectPath path to object
     * @return instance of {@link ObjectDescriptor}
     */
    @Override
    public ObjectDescriptor objectDescriptor(String objectPath) {
        return FileSystemDescriptor.of(fs, objectPath);
    }
}
