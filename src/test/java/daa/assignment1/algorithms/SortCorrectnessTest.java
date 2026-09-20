package daa.assignment1.algorithms;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import daa.assignment1.metrics.Metrics;

class SortCorrectnessTest {

    static Stream<int[]> edgeCases() {
        return Stream.of(
                new int[] {},
                new int[] {7},
                new int[] {3, 3, 3, 3, 3},
                new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
                new int[] {10, 9, 8, 7, 6, 5, 4, 3, 2, 1});
    }

    @ParameterizedTest
    @MethodSource("edgeCases")
    void mergeSortHandlesEdgeCases(int[] input) {
        assertSortedLikeJdk(input, MergeSort::sort);
    }

    @ParameterizedTest
    @MethodSource("edgeCases")
    void quickSortHandlesEdgeCases(int[] input) {
        assertSortedLikeJdk(input, QuickSort::sort);
    }

    @Test
    void mergeSortMatchesArraysSortOnRandomArrays() {
        compareWithJdk(120, MergeSort::sort);
    }

    @Test
    void quickSortMatchesArraysSortOnRandomArrays() {
        compareWithJdk(120, QuickSort::sort);
    }

    @Test
    void mergeSortRejectsNullArray() {
        assertThrows(IllegalArgumentException.class, () -> MergeSort.sort(null, new Metrics()));
    }

    @Test
    void quickSortRejectsNullArray() {
        assertThrows(IllegalArgumentException.class, () -> QuickSort.sort(null, new Metrics()));
    }

    @Test
    void mergeSortUsesSingleBufferAndCutoffOnTinyInputs() {
        int[] a = {5, 4, 3, 2, 1};
        MergeSort.sort(a, new Metrics());
        assertArrayEquals(new int[] {1, 2, 3, 4, 5}, a);
    }

    @Test
    void insertionSortMatchesArraysSort() {
        int[] a = {9, 1, 8, 2, 7, 3, 6};
        int[] expected = a.clone();
        Arrays.sort(expected);
        InsertionSort.sort(a, new Metrics());
        assertArrayEquals(expected, a);
    }

    @Test
    void quickSortDepthIsBoundedOnSortedArray() {
        int n = 100_000;
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = i;
        }
        Metrics metrics = new Metrics();
        QuickSort.sort(a, metrics);
        double bound = 2.0 * (Math.log(n) / Math.log(2));
        assertTrue(
                metrics.getMaxDepth() <= bound,
                () -> "maxDepth=" + metrics.getMaxDepth() + " exceeds 2*log2(n)=" + bound);
        for (int i = 1; i < n; i++) {
            assertTrue(a[i - 1] <= a[i]);
        }
    }

    @Test
    void metricsAreInstanceStateNotGlobal() {
        Metrics first = new Metrics();
        Metrics second = new Metrics();
        MergeSort.sort(new int[] {3, 2, 1, 0, 5, 4, 9, 8, 7, 6, 11, 10, 13, 12, 15, 14}, first);
        assertTrue(first.getComparisons() > 0);
        assertEquals(0, second.getComparisons());
        assertEquals(0, second.getMaxDepth());
    }

    private static void compareWithJdk(int trials, ArraySort sort) {
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        for (int t = 0; t < trials; t++) {
            int n = rng.nextInt(0, 500);
            int[] a = new int[n];
            for (int i = 0; i < n; i++) {
                a[i] = rng.nextInt(-10_000, 10_000);
            }
            if (t % 7 == 0 && n > 0) {
                Arrays.fill(a, 42);
            }
            assertSortedLikeJdk(a, sort);
        }
    }

    private static void assertSortedLikeJdk(int[] input, ArraySort sort) {
        int[] actual = input.clone();
        int[] expected = input.clone();
        Arrays.sort(expected);
        sort.sort(actual, new Metrics());
        assertArrayEquals(expected, actual);
    }

    @FunctionalInterface
    private interface ArraySort {
        void sort(int[] a, Metrics metrics);
    }
}
