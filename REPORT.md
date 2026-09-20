# Assignment 1 Report: Divide and Conquer & Asymptotic Notations

**Course:** Design and Analysis of Algorithms  
**Student:** Daniyar Makhmetov  
**Environment:** Java 17+, Maven, macOS

This report covers the implementation of MergeSort, QuickSort, and QuickSelect in Java. I measured their performance across array sizes n in {10^3, 10^4, 10^5, 10^6} and three input types (random, sorted, duplicates). Results below are based on the median of 5 benchmark runs.

---

## 1. Asymptotic Bounds

| Algorithm | Best | Average | Worst | Explanation |
|---|---|---|---|---|
| Insertion Sort | O(n) | O(n^2) | O(n^2) | Best case is an already sorted array. Worst case is reverse sorted. |
| MergeSort | O(n log n) | O(n log n) | O(n log n) | Always splits in half. Cutoff only changes small subarray behavior. |
| QuickSort (3-way + random pivot) | O(n) | O(n log n) | O(n^2) | Best case is all equal values (3-way partition finishes in 1 pass). Worst case is extremely rare due to random pivot. |
| QuickSelect (3-way + random pivot) | O(n) | O(n) | O(n^2) | Best case is equal values or picking the target element on the first partition. Searches only one side. |

Why sorted input is NOT QuickSort's worst case: I used a random pivot instead of picking the first element. Random pivot selection makes sorted arrays behave just like random permutations.

Why duplicate arrays are fast: Dijkstra's 3-way partition groups all equal elements into the middle section in O(n) time. They are skipped in subsequent recursive steps, keeping the max recursion depth around 2.

---

## 2. Recurrences and Master Theorem

Master Theorem formula: T(n) = a * T(n/b) + f(n).

### 2.1 MergeSort
T(n) = 2T(n/2) + O(n) for n > 15.
- a = 2, b = 2 => n^(log_2 2) = n^1 = n.
- f(n) = O(n).
- Matches Case 2 of the Master Theorem.
- Result: T(n) = O(n log n).

### 2.2 QuickSort (Balanced Split)
T(n) = 2T(n/2) + O(n).
- a = 2, b = 2, f(n) = O(n).
- Matches Case 2 of the Master Theorem.
- Result: T(n) = O(n log n) on average.
- To avoid stack overflow, the code always recurses into the smaller side first and processes the larger side in a while loop. This limits stack depth to O(log n).

### 2.3 QuickSelect
QuickSelect only calls recursion on one side: T(n) = T(n/2) + O(n).
- a = 1, b = 2 => n^(log_2 1) = n^0 = 1.
- f(n) = O(n) = Omega(n^(0 + 1)).
- Matches Case 3 of the Master Theorem (work at the root dominates).
- Result: T(n) = O(n).

---

## 3. Measured Results

All metrics are taken from results.csv using the median of 5 runs.

### 3.1 Execution Time (at n = 1,000,000)
- MergeSort: random = 81.5 ms, sorted = 20.3 ms, duplicates = 38.7 ms.
- QuickSort: random = 81.2 ms, sorted = 46.7 ms, duplicates = 11.6 ms.
- QuickSelect: random = 9.1 ms, sorted = 4.8 ms, duplicates = 8.1 ms.

Takeaways:
1. QuickSelect is about 10x faster than the sorting algorithms at n = 1,000,000, confirming O(n) vs O(n log n).
2. QuickSort on duplicate inputs is the fastest sort (11.6 ms) due to 3-way partitioning.
3. MergeSort is very fast on sorted input (20.3 ms) because sequential memory access fits CPU caching well.

### 3.2 Max Recursion Depth
- MergeSort: Grows predictably (9, 12, 15, 19 for n = 1,000 to 1,000,000), matching log_2 n minus the insertion sort cutoff frames.
- QuickSort: On random and sorted data, depth ranges from 6 to 13. For n = 100,000, measured depth was 11 (well under the 2 * log_2 n limit of ~33). On duplicates, depth stays at 2.
- QuickSelect: Depth follows a single search path (around 15-25 for random arrays).

### 3.3 Ratio Analysis
To check if empirical data matches theory, I analyzed the ratios:
- For sorts: ratio = comparisons / (n * log_2 n)
- For QuickSelect: ratio = comparisons / n

- MergeSort (random): Ratio converges smoothly to ~1.00 as n reaches 1,000,000. Setting c1 = 0.9 and c2 = 1.1 bounds the comparisons nicely for n >= 1000.
- QuickSort (random): Ratio stays in the range of 1.57 to 1.89. The ratio is stable as n grows, confirming O(n log n).
- QuickSelect (random): Ratio stays between 3.2 and 5.1, showing that comparisons scale linearly as a small constant multiple of n, confirming O(n).

---

## 4. Theory vs. Practice

The measured comparison counts and recursion depths closely match the Master Theorem predictions. Small differences in execution time are due to real-world system behavior:

1. JIT & Warm-up: The first runs are slower because Java compiles bytecode at runtime. Taking the median of 5 runs helped reduce this noise.
2. Garbage Collection: Allocating a single reusable helper array in MergeSort prevented GC overhead during recursive calls.
3. CPU Cache: Sequential array access on sorted data runs faster because of L1/L2 cache prefetching.
4. Insertion Sort Cutoff: Switching to insertion sort for subarrays n <= 15 eliminates unnecessary recursion overhead and speeds up execution.

---

## 5. How to Run

1. Run Unit Tests: `mvn test`
2. Run Benchmark: `mvn compile exec:java -Dexec.mainClass="daa.assignment1.benchmark.Benchmark"`
3. Generate Plots: `python3 generate_plots.py`