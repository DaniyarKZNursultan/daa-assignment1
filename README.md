# Assignment 1 — Divide and Conquer (DAA)

Java 17 / Maven project for Assignment 1 (Design and Analysis of Algorithms). It contains implementations of MergeSort, QuickSort, and QuickSelect, along with performance metrics, JUnit 5 tests, and benchmarks.

## What's Included

* **MergeSort** — Single allocated helper buffer with an InsertionSort cutoff for small sub-arrays ($n \le 15$).
* **QuickSort** — 3-way partitioning, random pivot selection, and recursion depth bounding.
* **QuickSelect** — Finding the k-th smallest element in linear time.
* **Metrics** — Tracks execution time, comparisons, and recursion depth.
* **JUnit 5 Tests** — Correctness tests and recursion depth checks.
* **Benchmark & CSV** — Runs algorithms on array sizes from $1,000$ to $1,000,000$ and exports median results to `results.csv`.

---

## How to Run

### 1. Run Unit Tests
```bash
mvn test
```

### 2. Run Benchmarks
Gathers performance metrics and writes results to results.csv:
```bash
mvn exec:java -Dexec.mainClass="daa.assignment1.benchmark.Benchmark"
```

### 3. Generate Plots 
To generate PNG charts from the CSV data (requires Python 3 and matplotlib):
```bash
python3 generate_plots.py
```

### Project Structure
```
src/
main/java/daa/assignment1/
algorithms/    # MergeSort, QuickSort, QuickSelect, InsertionSort, Partition
metrics/       # Metrics class for tracking time and steps
benchmark/     # Benchmarking code and CSV exporter
test/java/       # Tests for algorithm correctness and edge cases
```