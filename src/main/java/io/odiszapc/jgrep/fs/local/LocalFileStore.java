package io.odiszapc.jgrep.fs.local;

import io.odiszapc.jgrep.fs.ObjectDescriptor;
import io.odiszapc.jgrep.fs.ObjectStore;

public class LocalFileStore implements ObjectStore {

    private static final LocalFileStore instance = new LocalFileStore();

    private LocalFileStore() {
    }

    public static LocalFileStore instance() {
        return instance;
    }

    @Override
    public ObjectDescriptor objectDescriptor(String objectPath) {
        return LocalFsDescriptor.of(objectPath);
    }
}
