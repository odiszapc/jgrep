package io.odiszapc.jgrep.output;

import io.odiszapc.jgrep.format.LineFormatter;
import io.odiszapc.jgrep.pojo.LineMatch;

/**
 * Output to stdout
 */
public class StdoutPrinter implements OutputPrinter {

    private final LineFormatter lineFormatter;

    public StdoutPrinter(LineFormatter lineFormatter) {
        this.lineFormatter = lineFormatter;
    }

    @Override
    public void accept(LineMatch match) {
        System.out.println(lineFormatter.format(match));
    }
}
