package io.odiszapc.jgrep.lookup;

import io.odiszapc.jgrep.fs.ObjectDescriptor;
import io.odiszapc.jgrep.matcher.Matcher;
import io.odiszapc.jgrep.output.Output;
import io.odiszapc.jgrep.pojo.LineMatch;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.function.BiFunction;

/**
 * Search text inside object line-by-line
 */
public class TextSearch {
    private final ObjectDescriptor path;
    private final Matcher matcher;
    private final Output output;
    private final BiFunction<Integer, Integer, Void> onCompleted;

    public TextSearch(ObjectDescriptor path,
                      Matcher matcher,
                      Output output,
                      BiFunction<Integer, Integer, Void> onCompleted) {
        this.path = path;
        this.matcher = matcher;
        this.output = output;
        this.onCompleted = onCompleted;
    }

    /**
     * Start search for single file
     *
     */
    public void run() throws IOException {
        final Scanner lines = new Scanner(path.toObject().is(), StandardCharsets.UTF_8);

        int linesProcessed = 0;
        int linesMatched = 0;
        while (lines.hasNextLine()) {
            linesProcessed++;
            final String line = lines.nextLine();
            if (matcher.match(line)) {
                linesMatched++;
                output.accept(LineMatch.of(path, linesProcessed, line));
            }
        }

        // Scanner suppresses exceptions so we re-throw this
        if (lines.ioException() != null) {
            throw lines.ioException();
        }

        onCompleted.apply(linesProcessed, linesMatched);
    }
}
