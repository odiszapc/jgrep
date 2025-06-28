package io.odiszapc.jgrep.pojo;

import io.odiszapc.jgrep.fs.ObjectDescriptor;

import java.util.Objects;

/**
 * POJO represents matched line from specific file along with line number
 */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineMatch lineMatch = (LineMatch) o;
        return objectPath.equals(lineMatch.objectPath) && lineNum.equals(lineMatch.lineNum) && line.equals(lineMatch.line);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectPath, lineNum, line);
    }
}
