package io.odiszapc.jgrep.fs;

import io.odiszapc.jgrep.fs.local.FileSystemStore;

public interface ObjectStore {
    ObjectDescriptor objectDescriptor(String path);

    static ObjectStore defaultStore() {
        return FileSystemStore.defaultFS();
    }
}
