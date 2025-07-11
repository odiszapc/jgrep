package io.odiszapc.jgrep.output;

import io.odiszapc.jgrep.match.LineMatch;

/**
 * Formatter implementation for Linux Grep styled formatting rules
 */
public class LinuxGrepLineLineFormatter implements LineFormatter {

    /**
     * Format the line matching result
     *
     * @param match line matched
     * @return String formatted
     */
    public String format(LineMatch match) {
        return match.getObjectPath().name() + ':' + match.getLineNumber() + ':' + match.getLine();
    }
}
