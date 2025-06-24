package io.odiszapc.jgrep;

import io.odiszapc.jgrep.fs.ObjectStore;
import io.odiszapc.jgrep.lookup.Grep;

import java.util.concurrent.ExecutionException;

import static java.lang.System.exit;

public class Main {
    /**
     * The entrypoint.
     *
     * We don't use any command line parsing library or rich parsing and tuning
     * as we're focused on a concurrency part in this task
     *
     * @param args
     */
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        if (args.length < 2) {
            System.out.println("Invalid arguments. Exiting.");
            exit(1);
        }

        final String root = args[0];
        final String pattern = args[1];

        Grep.plainSearch(ObjectStore.defaultStore(), root, pattern, 4);
    }
}
