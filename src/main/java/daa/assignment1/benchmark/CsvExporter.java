package daa.assignment1.benchmark;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Writes benchmark rows to {@code results.csv}.
 */
public final class CsvExporter {

    public static final String HEADER = "algorithm,input,n,time_ms,comparisons,max_depth";

    private CsvExporter() {
    }

    public static void write(Path path, List<Row> rows) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add(HEADER);
        for (Row row : rows) {
            lines.add(row.toCsv());
        }
        Files.write(path, lines, StandardCharsets.UTF_8);
    }

    public record Row(
            String algorithm,
            String input,
            int n,
            double timeMs,
            long comparisons,
            int maxDepth) {

        public String toCsv() {
            return String.format(
                    Locale.US,
                    "%s,%s,%d,%.4f,%d,%d",
                    algorithm,
                    input,
                    n,
                    timeMs,
                    comparisons,
                    maxDepth);
        }
    }
}
