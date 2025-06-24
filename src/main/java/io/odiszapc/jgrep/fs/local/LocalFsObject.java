package io.odiszapc.jgrep.fs.local;

import io.odiszapc.jgrep.fs.ObjectData;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public class LocalFsObject implements ObjectData {

    private final Path filePath;

    public LocalFsObject(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public long size() {
        return filePath.toFile().length();
    }

    @Override
    public InputStream is() throws IOException {
        return new FileInputStream(filePath.toFile());
    }
}
