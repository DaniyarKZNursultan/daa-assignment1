package daa.assignment1.metrics;

/**
 * Per-run performance counters. Pass an instance into each algorithm; never
 * store metrics in static fields.
 */
public final class Metrics {

    private long comparisons;
    private int currentDepth;
    private int maxDepth;
    private long startNanos;
    private long elapsedNanos;

    public void reset() {
        comparisons = 0;
        currentDepth = 0;
        maxDepth = 0;
        startNanos = 0;
        elapsedNanos = 0;
    }

    public void startTimer() {
        startNanos = System.nanoTime();
    }

    public void stopTimer() {
        elapsedNanos = System.nanoTime() - startNanos;
    }

    public void enter() {
        currentDepth++;
        if (currentDepth > maxDepth) {
            maxDepth = currentDepth;
        }
    }

    public void exit() {
        currentDepth--;
    }

    public void incrementComparisons() {
        comparisons++;
    }

    public void addComparisons(long count) {
        if (count < 0) {
            throw new IllegalArgumentException("comparison count must be non-negative");
        }
        comparisons += count;
    }

    public long getComparisons() {
        return comparisons;
    }

    public int getCurrentDepth() {
        return currentDepth;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public long getElapsedNanos() {
        return elapsedNanos;
    }

    public double getElapsedMillis() {
        return elapsedNanos / 1_000_000.0;
    }
}
