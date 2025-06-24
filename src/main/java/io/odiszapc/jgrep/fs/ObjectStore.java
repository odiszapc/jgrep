package io.odiszapc.jgrep.fs;

import io.odiszapc.jgrep.fs.local.LocalFileStore;

public interface ObjectStore {
    ObjectDescriptor objectDescriptor(String path);

    static ObjectStore defaultStore() {
        return LocalFileStore.instance();
    }
}
