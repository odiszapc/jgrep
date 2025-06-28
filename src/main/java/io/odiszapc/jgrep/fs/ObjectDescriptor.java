package io.odiszapc.jgrep.fs;

import java.io.IOException;
import java.io.InputStream;

public interface ObjectDescriptor {

    boolean isContainer();

    InputStream inputStream() throws IOException;

    String name();

    long size() throws IOException;

    ObjectsIterable<? extends ObjectDescriptor> children() throws IOException;

}
