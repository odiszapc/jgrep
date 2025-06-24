package io.odiszapc.jgrep.matcher;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MatcherTest {

    @Test
    public void testSimpleMatcher() {
        Matcher matcher = new SimpleMatcher("hello");
        assertTrue(matcher.match("hello world"));
        assertFalse(matcher.match("goodbye world"));
    }

    @Test
    public void testIgnoreCaseMatcher() {
        Matcher matcher = new IgnoreCaseMatcher("HELLO");
        assertTrue(matcher.match("hello world"));
        assertTrue(matcher.match("HELLO WORLD"));
        assertFalse(matcher.match("goodbye world"));
    }

    @Test
    public void testRegexMatcher() {
        Matcher matcher = new RegexMatcher("h.llo");
        assertTrue(matcher.match("hello"));
        assertTrue(matcher.match("hallo"));
        assertFalse(matcher.match("hexlo world"));
    }
}
