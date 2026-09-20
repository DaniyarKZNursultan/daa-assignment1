package daa.assignment1.algorithms;

import java.util.concurrent.ThreadLocalRandom;

import daa.assignment1.metrics.Metrics;

/**
 * Shared 3-way partition used by QuickSort and QuickSelect.
 * After {@link #threeWay(int[], int, int, Metrics)}, {@code a[lo..lt-1] < pivot},
 * {@code a[lt..gt] == pivot}, {@code a[gt+1..hi] > pivot}.
 */
public final class Partition {

    public record Range(int lt, int gt) {
    }

    private Partition() {
    }

    public static Range threeWay(int[] a, int lo, int hi, Metrics metrics) {
        if (lo > hi) {
            throw new IllegalArgumentException("empty partition range");
        }
        int pivotIndex = lo + ThreadLocalRandom.current().nextInt(hi - lo + 1);
        int pivot = a[pivotIndex];
        swap(a, lo, pivotIndex);

        int lt = lo;
        int i = lo + 1;
        int gt = hi;
        while (i <= gt) {
            metrics.incrementComparisons();
            if (a[i] < pivot) {
                swap(a, lt, i);
                lt++;
                i++;
            } else {
                metrics.incrementComparisons();
                if (a[i] > pivot) {
                    swap(a, i, gt);
                    gt--;
                } else {
                    i++;
                }
            }
        }
        return new Range(lt, gt);
    }

    private static void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }
}
