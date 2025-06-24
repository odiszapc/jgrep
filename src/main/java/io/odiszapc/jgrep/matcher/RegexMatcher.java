package io.odiszapc.jgrep.matcher;

import java.util.regex.Pattern;

public class RegexMatcher implements Matcher {
    private final Pattern regex;

    public RegexMatcher(String pattern) {
        this.regex = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
    }

    public boolean match(String line) {
        return regex.matcher(line).find();
    }
}
