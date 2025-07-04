package io.odiszapc.jgrep.lookup;

import io.odiszapc.jgrep.fs.ObjectDescriptor;

/**
 * Interface for handler that is invoked when Directory traverse logic encounters an object (file)
 */
public interface ObjectVisitor {
    void process(ObjectDescriptor filePath);
}
