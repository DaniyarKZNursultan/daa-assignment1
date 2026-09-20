package daa.assignment1.algorithms;

import daa.assignment1.metrics.Metrics;

/**
 * MergeSort with a single reusable helper buffer and insertion-sort cutoff.
 */
public final class MergeSort {

    public static final int INSERTION_CUTOFF = 15;

    private MergeSort() {
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
        int[] helper = new int[a.length];
        sort(a, helper, 0, a.length - 1, metrics);
    }

    private static void sort(int[] a, int[] helper, int lo, int hi, Metrics metrics) {
        metrics.enter();
        try {
            if (hi - lo + 1 <= INSERTION_CUTOFF) {
                InsertionSort.sort(a, lo, hi, metrics);
                return;
            }
            int mid = lo + (hi - lo) / 2;
            sort(a, helper, lo, mid, metrics);
            sort(a, helper, mid + 1, hi, metrics);
            merge(a, helper, lo, mid, hi, metrics);
        } finally {
            metrics.exit();
        }
    }

    private static void merge(int[] a, int[] helper, int lo, int mid, int hi, Metrics metrics) {
        int left = lo;
        int right = mid + 1;
        int dest = lo;
        System.arraycopy(a, lo, helper, lo, hi - lo + 1);

        while (left <= mid && right <= hi) {
            metrics.incrementComparisons();
            if (helper[left] <= helper[right]) {
                a[dest++] = helper[left++];
            } else {
                a[dest++] = helper[right++];
            }
        }
        while (left <= mid) {
            a[dest++] = helper[left++];
        }
        while (right <= hi) {
            a[dest++] = helper[right++];
        }
    }
}
