package io.odiszapc.jgrep.stats;

public class StdoutStatisticPrinter implements StatisticsPrinter {
    @Override
    public void print(Statistics statistics) {
        System.out.println("\n--- STATISTICS ---");
        System.out.println("Files processed: " + statistics.filesProcessed());
        System.out.println("Matches found: " + statistics.linesMatched());
        System.out.println("Lines processed: " + statistics.linesProcessed());
        System.out.println("Bytes processed: " + statistics.bytesProcessed());
    }
}
