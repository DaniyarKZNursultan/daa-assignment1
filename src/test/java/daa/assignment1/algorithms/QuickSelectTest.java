package daa.assignment1.algorithms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.Test;

import daa.assignment1.metrics.Metrics;

class QuickSelectTest {

    @Test
    void matchesSortedKOnRandomArrays() {
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        for (int t = 0; t < 120; t++) {
            int n = rng.nextInt(1, 400);
            int[] a = new int[n];
            for (int i = 0; i < n; i++) {
                a[i] = rng.nextInt(-50_000, 50_000);
            }
            if (t % 11 == 0) {
                Arrays.fill(a, t);
            }
            int k = rng.nextInt(n);
            int[] expected = a.clone();
            Arrays.sort(expected);
            int got = QuickSelect.select(a.clone(), k);
            assertEquals(expected[k], got);
        }
    }

    @Test
    void handlesSingleElement() {
        assertEquals(42, QuickSelect.select(new int[] {42}, 0));
    }

    @Test
    void handlesAllEqual() {
        int[] a = {7, 7, 7, 7, 7};
        assertEquals(7, QuickSelect.select(a, 3));
    }

    @Test
    void handlesAlreadySorted() {
        int[] a = {1, 2, 3, 4, 5};
        assertEquals(1, QuickSelect.select(a.clone(), 0));
        assertEquals(5, QuickSelect.select(a.clone(), 4));
    }

    @Test
    void rejectsEmptyArray() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> QuickSelect.select(new int[] {}, 0));
        assertTrue(ex.getMessage().toLowerCase().contains("empty"));
    }

    @Test
    void rejectsNullArray() {
        assertThrows(IllegalArgumentException.class, () -> QuickSelect.select(null, 0));
    }

    @Test
    void rejectsKOutOfBounds() {
        IllegalArgumentException low = assertThrows(
                IllegalArgumentException.class,
                () -> QuickSelect.select(new int[] {1, 2, 3}, -1));
        assertTrue(low.getMessage().contains("out of bounds"));
        IllegalArgumentException high = assertThrows(
                IllegalArgumentException.class,
                () -> QuickSelect.select(new int[] {1, 2, 3}, 3));
        assertTrue(high.getMessage().contains("out of bounds"));
    }

    @Test
    void recordsMetricsOnInstrumentedCall() {
        Metrics metrics = new Metrics();
        int[] a = {9, 1, 8, 2, 7, 3};
        int value = QuickSelect.select(a, 2, metrics);
        int[] sorted = {9, 1, 8, 2, 7, 3};
        Arrays.sort(sorted);
        assertEquals(sorted[2], value);
        assertTrue(metrics.getComparisons() > 0);
        assertTrue(metrics.getMaxDepth() >= 1);
    }
}
