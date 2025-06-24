package io.odiszapc.jgrep.output;

import io.odiszapc.jgrep.format.LineFormatter;
import io.odiszapc.jgrep.pojo.LineMatch;

public class StdoutPrinter implements Output {

    private final LineFormatter lineFormatter;

    public StdoutPrinter(LineFormatter lineFormatter) {
        this.lineFormatter = lineFormatter;
    }

    @Override
    public void accept(LineMatch match) {
        System.out.println(lineFormatter.format(match));
    }
}
