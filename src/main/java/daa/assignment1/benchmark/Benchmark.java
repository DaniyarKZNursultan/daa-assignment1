package daa.assignment1.benchmark;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;

import daa.assignment1.algorithms.MergeSort;
import daa.assignment1.algorithms.QuickSelect;
import daa.assignment1.algorithms.QuickSort;
import daa.assignment1.metrics.Metrics;

/**
 * Runs MergeSort, QuickSort and QuickSelect on the assignment input families,
 * takes the median of five timings, and writes {@code results.csv}.
 */
public final class Benchmark {

    private static final int[] SIZES = {1_000, 10_000, 100_000, 1_000_000};
    private static final String[] INPUTS = {"random", "sorted", "duplicates"};
    private static final int RUNS = 5;

    public static void main(String[] args) throws Exception {
        Path output = args.length > 0 ? Path.of(args[0]) : Path.of("results.csv");
        List<CsvExporter.Row> rows = runAll();
        CsvExporter.write(output, rows);
        System.out.println("Wrote " + rows.size() + " rows to " + output.toAbsolutePath());
    }

    static List<CsvExporter.Row> runAll() {
        List<CsvExporter.Row> rows = new ArrayList<>();
        rows.addAll(benchmarkSort("MergeSort", MergeSort::sort));
        rows.addAll(benchmarkSort("QuickSort", QuickSort::sort));
        rows.addAll(benchmarkSelect());
        return rows;
    }

    private static List<CsvExporter.Row> benchmarkSort(String name, BiConsumer<int[], Metrics> sort) {
        List<CsvExporter.Row> rows = new ArrayList<>();
        for (String input : INPUTS) {
            for (int n : SIZES) {
                int[] base = generate(input, n);
                Sample[] samples = new Sample[RUNS];
                for (int r = 0; r < RUNS; r++) {
                    int[] copy = Arrays.copyOf(base, base.length);
                    Metrics metrics = new Metrics();
                    metrics.startTimer();
                    sort.accept(copy, metrics);
                    metrics.stopTimer();
                    samples[r] = new Sample(metrics.getElapsedMillis(), metrics.getComparisons(), metrics.getMaxDepth());
                }
                Sample median = medianByTime(samples);
                rows.add(new CsvExporter.Row(
                        name, input, n, median.timeMs, median.comparisons, median.maxDepth));
                System.out.printf("  %s %s n=%d  %.3f ms%n", name, input, n, median.timeMs);
            }
        }
        return rows;
    }

    private static List<CsvExporter.Row> benchmarkSelect() {
        List<CsvExporter.Row> rows = new ArrayList<>();
        for (String input : INPUTS) {
            for (int n : SIZES) {
                int[] base = generate(input, n);
                int k = n / 2;
                Sample[] samples = new Sample[RUNS];
                for (int r = 0; r < RUNS; r++) {
                    int[] copy = Arrays.copyOf(base, base.length);
                    Metrics metrics = new Metrics();
                    metrics.startTimer();
                    QuickSelect.select(copy, k, metrics);
                    metrics.stopTimer();
                    samples[r] = new Sample(metrics.getElapsedMillis(), metrics.getComparisons(), metrics.getMaxDepth());
                }
                Sample median = medianByTime(samples);
                rows.add(new CsvExporter.Row(
                        "QuickSelect", input, n, median.timeMs, median.comparisons, median.maxDepth));
                System.out.printf("  QuickSelect %s n=%d  %.3f ms%n", input, n, median.timeMs);
            }
        }
        return rows;
    }

    static int[] generate(String input, int n) {
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        int[] a = new int[n];
        switch (input) {
            case "random" -> {
                for (int i = 0; i < n; i++) {
                    a[i] = rng.nextInt();
                }
            }
            case "sorted" -> {
                for (int i = 0; i < n; i++) {
                    a[i] = i;
                }
            }
            case "duplicates" -> {
                for (int i = 0; i < n; i++) {
                    a[i] = rng.nextInt(10);
                }
            }
            default -> throw new IllegalArgumentException("unknown input type: " + input);
        }
        return a;
    }

    private static Sample medianByTime(Sample[] samples) {
        Arrays.sort(samples, Comparator.comparingDouble(s -> s.timeMs));
        return samples[samples.length / 2];
    }

    private record Sample(double timeMs, long comparisons, int maxDepth) {
    }
}
