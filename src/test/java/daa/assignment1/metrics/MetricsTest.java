package daa.assignment1.metrics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class MetricsTest {

    @Test
    void tracksDepthAndComparisonsPerInstance() {
        Metrics metrics = new Metrics();
        metrics.enter();
        metrics.enter();
        metrics.incrementComparisons();
        metrics.incrementComparisons();
        metrics.exit();
        assertEquals(1, metrics.getCurrentDepth());
        assertEquals(2, metrics.getMaxDepth());
        assertEquals(2, metrics.getComparisons());
        metrics.exit();
        assertEquals(0, metrics.getCurrentDepth());
        assertEquals(2, metrics.getMaxDepth());
    }

    @Test
    void timerUsesNanoTime() {
        Metrics metrics = new Metrics();
        metrics.startTimer();
        long sink = 0;
        for (int i = 0; i < 10_000; i++) {
            sink += i;
        }
        metrics.stopTimer();
        assertTrue(sink >= 0);
        assertTrue(metrics.getElapsedNanos() >= 0);
        assertTrue(metrics.getElapsedMillis() >= 0);
    }

    @Test
    void resetClearsAllCounters() {
        Metrics metrics = new Metrics();
        metrics.enter();
        metrics.incrementComparisons();
        metrics.startTimer();
        metrics.stopTimer();
        metrics.reset();
        assertEquals(0, metrics.getComparisons());
        assertEquals(0, metrics.getMaxDepth());
        assertEquals(0, metrics.getCurrentDepth());
        assertEquals(0, metrics.getElapsedNanos());
    }
}
