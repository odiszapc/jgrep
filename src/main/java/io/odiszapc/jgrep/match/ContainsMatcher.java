package io.odiszapc.jgrep.match;

public class ContainsMatcher implements Matcher {
    protected final String pattern;

    public ContainsMatcher(String pattern) {
        this.pattern = pattern;
    }

    public boolean match(String line) {
        return line.contains(pattern);
    }
}
