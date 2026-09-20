import csv
import math
from collections import defaultdict
from pathlib import Path

import matplotlib

matplotlib.use("Agg")
import matplotlib.pyplot as plt

ROOT = Path(__file__).resolve().parent
CSV_PATH = ROOT / "results.csv"
PLOTS = ROOT / "plots"


def load_rows():
    with CSV_PATH.open(newline="") as f:
        return list(csv.DictReader(f))


def series(rows, y_fn):
    grouped = defaultdict(list)
    for row in rows:
        key = (row["algorithm"], row["input"])
        grouped[key].append((int(row["n"]), y_fn(row)))
    for key in grouped:
        grouped[key].sort()
    return grouped


def plot_lines(grouped, ylabel, title, outfile, log_x=True, log_y=False):
    plt.figure(figsize=(9, 5.5))
    for (algo, kind), points in sorted(grouped.items()):
        xs = [p[0] for p in points]
        ys = [p[1] for p in points]
        plt.plot(xs, ys, marker="o", label=f"{algo} / {kind}")
    if log_x:
        plt.xscale("log")
    if log_y:
        plt.yscale("log")
    plt.xlabel("n")
    plt.ylabel(ylabel)
    plt.title(title)
    plt.grid(True, which="both", linestyle="--", alpha=0.4)
    plt.legend(fontsize=8, ncol=2)
    plt.tight_layout()
    plt.savefig(outfile, dpi=160)
    plt.close()


def ratio(row):
    n = int(row["n"])
    comparisons = float(row["comparisons"])
    algo = row["algorithm"]
    if algo == "QuickSelect":
        return comparisons / n
    return comparisons / (n * math.log2(n))


def main():
    PLOTS.mkdir(exist_ok=True)
    rows = load_rows()
    plot_lines(
        series(rows, lambda r: float(r["time_ms"])),
        "time (ms)",
        "Time vs n",
        PLOTS / "time_vs_n.png",
        log_y=True,
    )
    plot_lines(
        series(rows, lambda r: int(r["max_depth"])),
        "max recursion depth",
        "Max recursion depth vs n",
        PLOTS / "depth_vs_n.png",
    )
    plot_lines(
        series(rows, ratio),
        "ratio",
        "Cost ratio vs n (comparisons / n log n, QuickSelect: comparisons / n)",
        PLOTS / "ratio_vs_n.png",
    )
    print(f"Wrote plots to {PLOTS}")


if __name__ == "__main__":
    main()
