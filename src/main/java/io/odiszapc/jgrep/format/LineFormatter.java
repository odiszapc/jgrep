package io.odiszapc.jgrep.format;

import io.odiszapc.jgrep.pojo.LineMatch;

public interface LineFormatter {
    default String format(LineMatch match) {
        return match.toString();
    }
}
