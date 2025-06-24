package io.odiszapc.jgrep.matcher;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MatcherTest {

    @Test
    public void testSimpleMatcher() {
        Matcher matcher = new SimpleMatcher("hello");
        assertTrue(matcher.matches("hello world"));
        assertFalse(matcher.matches("goodbye world"));
    }

    @Test
    public void testIgnoreCaseMatcher() {
        Matcher matcher = new IgnoreCaseMatcher("HELLO");
        assertTrue(matcher.matches("hello world"));
        assertTrue(matcher.matches("HELLO WORLD"));
        assertFalse(matcher.matches("goodbye world"));
    }

    @Test
    public void testRegexMatcher() {
        Matcher matcher = new RegexMatcher("h.llo");
        assertTrue(matcher.matches("hello"));
        assertTrue(matcher.matches("hallo"));
        assertFalse(matcher.matches("hxllo world"));
    }
}
