package io.odiszapc.jgrep;

import io.odiszapc.jgrep.fs.ObjectStore;

import static java.lang.System.exit;

public class Main {
    /**
     * The entrypoint.
     * <p>
     * We don't use any command line parsing library or rich parsing and tuning
     * as we're focused on a concurrency part in this task
     *
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Invalid arguments. Exiting.");
            exit(1);
        }

        final String root = args[0];
        final String pattern = args[1];

        JGrep.plainSearch(ObjectStore.defaultStore(), root, pattern, 4);
    }
}
