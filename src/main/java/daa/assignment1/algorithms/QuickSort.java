package daa.assignment1.algorithms;

import daa.assignment1.metrics.Metrics;

/**
 * QuickSort with a random pivot, 3-way partitioning, and bounded recursion:
 * the smaller side is solved recursively and the larger side is handled in a loop.
 */
public final class QuickSort {

    private QuickSort() {
    }

    public static void sort(int[] a, Metrics metrics) {
        if (a == null) {
            throw new IllegalArgumentException("array must not be null");
        }
        if (metrics == null) {
            throw new IllegalArgumentException("metrics must not be null");
        }
        if (a.length < 2) {
            return;
        }
        sort(a, 0, a.length - 1, metrics);
    }

    private static void sort(int[] a, int lo, int hi, Metrics metrics) {
        while (lo < hi) {
            metrics.enter();
            try {
                Partition.Range range = Partition.threeWay(a, lo, hi, metrics);
                int leftSize = range.lt() - lo;
                int rightSize = hi - range.gt();
                if (leftSize < rightSize) {
                    if (leftSize > 1) {
                        sort(a, lo, range.lt() - 1, metrics);
                    }
                    lo = range.gt() + 1;
                } else {
                    if (rightSize > 1) {
                        sort(a, range.gt() + 1, hi, metrics);
                    }
                    hi = range.lt() - 1;
                }
            } finally {
                metrics.exit();
            }
        }
    }
}
