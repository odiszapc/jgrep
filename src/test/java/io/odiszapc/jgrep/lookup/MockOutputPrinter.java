package io.odiszapc.jgrep.lookup;

import io.odiszapc.jgrep.output.LinuxGrepLineLineFormatter;
import io.odiszapc.jgrep.output.OutputPrinter;
import io.odiszapc.jgrep.pojo.LineMatch;

import java.util.ArrayList;

class MockOutputPrinter implements OutputPrinter {

    ArrayList<String> matches;

    public MockOutputPrinter() {
        matches = new ArrayList<>();
    }

    @Override
    public void accept(LineMatch lineMatch) {
        final String formattedEntry = new LinuxGrepLineLineFormatter().format(lineMatch);
        matches.add(formattedEntry);

        System.out.println(formattedEntry);
    }

    boolean contains(String line) {
        return matches.contains(line);
    }
}
