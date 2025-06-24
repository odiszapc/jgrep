package io.odiszapc.jgrep.format;

import io.odiszapc.jgrep.pojo.LineMatch;

public class LinuxGrepLineLineFormatter implements LineFormatter {

    public String format(LineMatch match) {
        return match.getObjectPath().objectName() + ':' + match.getLineNumber() + ':' + match.getLine();
    }
}
