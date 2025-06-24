package io.odiszapc.jgrep.output;

import io.odiszapc.jgrep.pojo.LineMatch;

import java.util.function.Consumer;

public interface Output extends Consumer<LineMatch> {

}
