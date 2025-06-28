package io.odiszapc.jgrep.output;

import io.odiszapc.jgrep.match.LineMatch;

import java.util.function.Consumer;

public interface OutputPrinter extends Consumer<LineMatch> {

}
