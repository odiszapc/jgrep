package io.odiszapc.jgrep.fs;

import java.io.IOException;
import java.io.InputStream;

public interface ObjectData {

    long size();

    InputStream is() throws IOException;
}
