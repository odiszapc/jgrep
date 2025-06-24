package io.odiszapc.jgrep.lookup;

import io.odiszapc.jgrep.fs.ObjectDescriptor;
import io.odiszapc.jgrep.fs.ObjectsIterable;

import java.io.IOException;

/**
 * This is expected to be single-threaded execution as traversing
 * Iteration itself over the directory tree wouldn't be sped up by doing it in multiple threads.
 * What we want to do is iterate over the directories in a single thread
 * and submit the work you are doing on files/directories
 */
public class DirTraverser {
    private final ObjectDescriptor parentContainer;

    public DirTraverser(ObjectDescriptor parent) {
        this.parentContainer = parent;
    }

    private static void iterateChildren(ObjectDescriptor containerPath,
                                        ObjectHandler objectHandler) throws IOException {
        try (ObjectsIterable<?> objects = containerPath.children()) {
            for (ObjectDescriptor object : objects) {
                if (object.isContainer()) {
                    iterateChildren(object, objectHandler);
                } else {
                    objectHandler.process(object);
                }
            }
        }
    }

    public static void start(ObjectDescriptor rootDir, ObjectHandler fileFoundHandler) {
        new DirTraverser(rootDir).run(fileFoundHandler);
    }

    public void run(ObjectHandler objectHandler) {
        try {
            iterateChildren(parentContainer, objectHandler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
