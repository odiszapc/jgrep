package io.odiszapc.jgrep.lookup;

import io.odiszapc.jgrep.fs.ObjectDescriptor;
import io.odiszapc.jgrep.fs.ObjectsIterable;

import java.io.IOException;

/**
 * This is expected to be single-threaded execution as traversing is done over HDD by default.
 * <p>
 * Iteration itself over the directory tree wouldn't speed up the whole process by doing it in multiple threads.
 * <p>
 * What we want to do is iterate over the directories in a single thread instead
 * and then submit each file to be parsed to a concurrent part of this software.
 */
public class ObjectTreeWalker {
    private final ObjectDescriptor parentContainer;

    public ObjectTreeWalker(ObjectDescriptor parent) {
        this.parentContainer = parent;
    }

    /**
     * Iterate over container children (which are files in case of directory)
     * Do it recursively.
     *
     * @param containerPath path to parent container
     * @param visitor       invoked each time we encounter an object (file)
     */
    private void walk(ObjectDescriptor containerPath,
                      ObjectVisitor visitor) throws IOException {
        try (ObjectsIterable<?> objects = containerPath.children()) {
            for (ObjectDescriptor object : objects) {
                if (object.isContainer()) {
                    walk(object, visitor);
                } else {
                    visitor.process(object);
                }
            }
        }
    }

    /**
     * Iterate over container children (which are files in case of directory)
     *
     * @param containerPath path to parent container
     * @param objectVisitor invoked each time we encounter an object (file)
     */
    public static void run(ObjectDescriptor containerPath, ObjectVisitor objectVisitor) {
        new ObjectTreeWalker(containerPath).run(objectVisitor);
    }

    /**
     * Iterate over container children (which are files in case of directory)
     *
     * @param objectVisitor invoked each time we encounter an object (file)
     */
    public void run(ObjectVisitor objectVisitor) {
        try {
            walk(parentContainer, objectVisitor);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
