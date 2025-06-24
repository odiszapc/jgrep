package io.odiszapc.jgrep.matcher;

public class SimpleMatcher implements Matcher {
    protected final String pattern;

    public SimpleMatcher(String pattern) {
        this.pattern = pattern;
    }

    public boolean match(String line) {
        return line.contains(pattern);
    }
}
