package daa.assignment1.algorithms;

import daa.assignment1.metrics.Metrics;

/**
 * In-place insertion sort used as the MergeSort cutoff for small subarrays.
 */
public final class InsertionSort {

    private InsertionSort() {
    }

    public static void sort(int[] a, Metrics metrics) {
        if (a == null) {
            throw new IllegalArgumentException("array must not be null");
        }
        if (metrics == null) {
            throw new IllegalArgumentException("metrics must not be null");
        }
        sort(a, 0, a.length - 1, metrics);
    }

    static void sort(int[] a, int lo, int hi, Metrics metrics) {
        metrics.enter();
        try {
            for (int i = lo + 1; i <= hi; i++) {
                int key = a[i];
                int j = i - 1;
                while (j >= lo) {
                    metrics.incrementComparisons();
                    if (a[j] <= key) {
                        break;
                    }
                    a[j + 1] = a[j];
                    j--;
                }
                a[j + 1] = key;
            }
        } finally {
            metrics.exit();
        }
    }
}
