package io.odiszapc.jgrep.format;

import io.odiszapc.jgrep.pojo.LineMatch;

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
        return match.getObjectPath().objectName() + ':' + match.getLineNumber() + ':' + match.getLine();
    }
}
