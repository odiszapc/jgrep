package io.odiszapc.jgrep.fs;

import java.io.IOException;

public interface ObjectDescriptor {

    boolean isContainer();

    ObjectData toObject();

    String objectName();

    String fullPath();

    ObjectsIterable<? extends ObjectDescriptor> children() throws IOException;

}
