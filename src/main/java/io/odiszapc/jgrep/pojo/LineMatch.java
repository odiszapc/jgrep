package io.odiszapc.jgrep.pojo;

import io.odiszapc.jgrep.fs.ObjectDescriptor;

public class LineMatch {
    private final ObjectDescriptor objectPath;
    private final Integer lineNum;
    private final String line;

    public static LineMatch of(ObjectDescriptor filePath, Integer lineNum, String line) {
        return new LineMatch(filePath, lineNum, line);
    }

    public LineMatch(ObjectDescriptor objectPath, Integer lineNum, String line) {
        this.objectPath = objectPath;
        this.lineNum = lineNum;
        this.line = line;
    }

    public ObjectDescriptor getObjectPath() {
        return objectPath;
    }

    public Integer getLineNumber() {
        return lineNum;
    }

    public String getLine() {
        return line;
    }
}
