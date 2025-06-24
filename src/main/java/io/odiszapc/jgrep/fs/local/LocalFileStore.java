package io.odiszapc.jgrep.fs.local;

import io.odiszapc.jgrep.fs.ObjectDescriptor;
import io.odiszapc.jgrep.fs.ObjectStore;

/**
 * Object store implementation for Local (Windows, *nix) file system
 */
public class LocalFileStore implements ObjectStore {

    private static final LocalFileStore instance = new LocalFileStore();

    private LocalFileStore() {
    }

    public static LocalFileStore instance() {
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
        return LocalFsDescriptor.of(objectPath);
    }
}
