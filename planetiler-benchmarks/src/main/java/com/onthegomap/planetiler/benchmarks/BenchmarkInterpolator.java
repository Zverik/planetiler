package com.onthegomap.planetiler.benchmarks;

import com.onthegomap.planetiler.util.Interpolator;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Supplier;

public class BenchmarkInterpolator {
  private static double sum = 0;

  public static void main(String[] args) {
    long times = 10_000_000;
    benchmarkInterpolator("linear", times, Interpolator::linear);
    benchmarkInterpolator("sqrt", times, Interpolator::sqrt);
    benchmarkInterpolator("pow2", times, () -> Interpolator.power().exponent(2));
    benchmarkInterpolator("pow", times, Interpolator::power);
    benchmarkInterpolator("log", times, Interpolator::log);
    benchmarkInterpolator("log2", times, () -> Interpolator.log().base(2));
    System.err.println(sum);
  }

  private static void benchmarkInterpolator(String name, long times, Supplier<Interpolator<?>> get) {
    benchmarkAndInverted(name + "_2", 1, 2, times, () -> get.get().put(1, 1).put(2, 2));
    benchmarkAndInverted(name + "_3", 1, 2, times, () -> get.get().put(1, 1).put(1.5, 2).put(2, 3));
  }

  private static void benchmarkAndInverted(String name, double start, double end, long steps,
    Supplier<Interpolator<?>> build) {
    benchmark(name + "_f", start, end, steps, build::get);
    benchmark(name + "_i", start, end, steps, () -> build.get().invert());
  }

  private static void benchmark(String name, double start, double end, long steps,
    Supplier<DoubleUnaryOperator> build) {
    long a = System.currentTimeMillis();
    double delta = (end - start) / steps;
    double x = start;
    double result = 0;
    for (long i = 0; i < steps; i++) {
      result += build.get().applyAsDouble(x += delta);
    }
    long b = System.currentTimeMillis();
    x = start;
    var preBuilt = build.get();
    for (long i = 0; i < steps; i++) {
      result += preBuilt.applyAsDouble(x += delta);
    }
    long c = System.currentTimeMillis();
    sum += result;
    System.err.println(name + "\t" + (b - a) + "\t" + (c - b));
  }
}
