package io.odiszapc.jgrep.lookup;

import io.odiszapc.jgrep.fs.ObjectDescriptor;

public interface ObjectHandler {

    void process(ObjectDescriptor filePath);
}
