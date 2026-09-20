# DAA Assignment 1 — Divide and Conquer

Java 17 Maven project: MergeSort (single helper buffer + insertion cutoff), QuickSort (random pivot, 3-way partition, bounded depth), and QuickSelect, with instance-level `Metrics`, JUnit 5 tests, and a CSV benchmark.

## Build

```bash
mvn -q test-compile
```

Requires **JDK 17+** and **Maven 3.9+**. If `java` is not on your PATH:

```bash
export JAVA_HOME="$(/usr/libexec/java_home 2>/dev/null || echo /opt/homebrew/opt/openjdk/libexec/openjdk.jdk/Contents/Home)"
```

## Tests

```bash
mvn test
```

Covered:

- MergeSort / QuickSort vs `Arrays.sort` on 120 random arrays plus empty, singleton, all-equal, sorted, and reverse-sorted inputs
- QuickSort recursion depth on a **sorted** array of 100,000 elements: `maxDepth <= 2 * log2(n)`
- QuickSelect vs `sorted[k]` on 120 random arrays, plus invalid `k` / empty array (`IllegalArgumentException`)

## Benchmark

Writes `results.csv` with columns `algorithm,input,n,time_ms,comparisons,max_depth`.

Sizes: `1000, 10000, 100000, 1000000`. Inputs: `random`, `sorted`, `duplicates` (values in `0..9`). Each cell is the **median of 5 runs**.

```bash
mvn -q exec:java
# or
mvn -q exec:java -Dexec.args=results.csv
```

A full run (including `n = 1_000_000`) takes a few minutes.

## Plots

```bash
python3 -m pip install matplotlib
python3 generate_plots.py
```

PNG files are written to `plots/`:

- `time_vs_n.png`
- `depth_vs_n.png`
- `ratio_vs_n.png`

## Layout

```
src/main/java/daa/assignment1/
  algorithms/   MergeSort, QuickSort, QuickSelect, InsertionSort, Partition
  metrics/      Metrics
  benchmark/    Benchmark, CsvExporter
src/test/java/daa/assignment1/
```

## Git

Working history lives on `main` (tag **`v1.0`**), merged from `feature/metrics`, `feature/mergesort`, `feature/quicksort`, `feature/select`, and `feature/benchmark`.
