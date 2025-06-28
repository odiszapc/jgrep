package io.odiszapc.jgrep.lookup;

import io.odiszapc.jgrep.match.LineMatch;
import io.odiszapc.jgrep.output.LinuxGrepLineLineFormatter;
import io.odiszapc.jgrep.output.OutputPrinter;

import java.util.ArrayList;

class MockOutputCollector implements OutputPrinter {

    ArrayList<String> matches;

    public MockOutputCollector() {
        matches = new ArrayList<>();
    }

    @Override
    public void accept(LineMatch lineMatch) {
        final String formattedEntry = new LinuxGrepLineLineFormatter().format(lineMatch);
        matches.add(formattedEntry);
    }

    boolean contains(String line) {
        return matches.contains(line);
    }
}
