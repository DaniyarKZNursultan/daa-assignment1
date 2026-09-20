package daa.assignment1.algorithms;

import daa.assignment1.metrics.Metrics;

/**
 * Randomized QuickSelect: returns the k-th smallest element (0-based).
 */
public final class QuickSelect {

    private QuickSelect() {
    }

    public static int select(int[] a, int k) {
        return select(a, k, new Metrics());
    }

    public static int select(int[] a, int k, Metrics metrics) {
        if (a == null) {
            throw new IllegalArgumentException("array must not be null");
        }
        if (metrics == null) {
            throw new IllegalArgumentException("metrics must not be null");
        }
        if (a.length == 0) {
            throw new IllegalArgumentException("array must not be empty");
        }
        if (k < 0 || k >= a.length) {
            throw new IllegalArgumentException(
                    "k=" + k + " is out of bounds for array of length " + a.length);
        }
        return select(a, 0, a.length - 1, k, metrics);
    }

    private static int select(int[] a, int lo, int hi, int k, Metrics metrics) {
        metrics.enter();
        try {
            if (lo == hi) {
                return a[lo];
            }
            Partition.Range range = Partition.threeWay(a, lo, hi, metrics);
            if (k < range.lt()) {
                return select(a, lo, range.lt() - 1, k, metrics);
            }
            if (k > range.gt()) {
                return select(a, range.gt() + 1, hi, k, metrics);
            }
            return a[k];
        } finally {
            metrics.exit();
        }
    }
}
