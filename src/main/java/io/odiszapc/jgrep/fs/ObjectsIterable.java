package io.odiszapc.jgrep.fs;

import java.io.Closeable;
import java.util.Iterator;

public interface ObjectsIterable<T extends ObjectDescriptor> extends Iterable<T>, Closeable {

    Iterator<T> it();

    @Override
    default Iterator<T> iterator() {
        return it();
    }
}
