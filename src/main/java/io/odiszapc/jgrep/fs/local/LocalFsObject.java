package io.odiszapc.jgrep.fs.local;

import io.odiszapc.jgrep.fs.ObjectData;
import io.odiszapc.jgrep.fs.ObjectDescriptor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * {@link ObjectData} abstraction implementation for local file system
 */
public class LocalFsObject implements ObjectData {

    private final Path filePath;

    public LocalFsObject(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Returns local file size in bytes
     * @return size in bytes
     */
    @Override
    public long size() {
        return filePath.toFile().length();
    }

    /**
     * Returns {@link InputStream} for reading data from local file
     * @return Instance of {@link InputStream}
     */
    @Override
    public InputStream is() throws IOException {
        return new FileInputStream(filePath.toFile());
    }
}
