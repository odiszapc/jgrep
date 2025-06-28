package io.odiszapc.jgrep.match;

public class IgnoreCaseMatcher extends ContainsMatcher {
    public IgnoreCaseMatcher(String pattern) {
        super(pattern.toLowerCase());
    }

    public boolean match(String line) {
        return super.match(line.toLowerCase());
    }
}
