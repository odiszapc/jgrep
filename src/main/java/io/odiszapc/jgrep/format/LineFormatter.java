package io.odiszapc.jgrep.format;

import io.odiszapc.jgrep.pojo.LineMatch;

/**
 * Abstraction layer over line formatting logic.
 * Defines contract for formatting output for a matched line.
 */
public interface LineFormatter {
    /**
     * Format the line match provided
     * @param match line matched
     * @return String formatted
     */
    default String format(LineMatch match) {
        return match.toString();
    }
}
