package io.odiszapc.jgrep;

import io.odiszapc.jgrep.fs.ObjectStore;
import io.odiszapc.jgrep.lookup.Grep;

import java.util.concurrent.ExecutionException;

import static java.lang.System.exit;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // Don't use any command line parsing library as we're focused on a concurrency part here
        if (args.length < 2) {
            System.out.println("Invalid arguments. Exiting.");
            exit(1);
        }

        final String root = args[0];
        final String pattern = args[1];

        Grep.plainSearch(ObjectStore.defaultStore(), root, pattern, 4);
    }
}
