package com.execodex.sparrowair2.datademo;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

/**
 * Test class for AircraftDataDemo3 buffer size benchmarking.
 * Note: This test is disabled by default as it's meant to be run manually
 * when you want to benchmark different buffer sizes.
 */
public class AircraftDataDemo3Test {

    @Test
    @Disabled("Enable manually when you want to run buffer size benchmarks")
    public void testBufferSizeBenchmark() {
        String dataPath = "stuff/data/iaka2.jsonl";
        int[] bufferSizes = {1024, 2048, 4096, 8192, 16384, 32768, 65536};
        int iterations = 3; // Reduced iterations for faster test execution

        System.out.println("Starting buffer size benchmark...");
        List<Map<String, Object>> benchmarkResults = AircraftDataDemo3.runBufferSizeBenchmarks(dataPath, bufferSizes, iterations);

        // Display results
        System.out.println("\nBenchmark Results:");
        System.out.println("=================");
        System.out.printf("%-12s %-15s %-15s%n", "Buffer Size", "Avg Time (ms)", "Avg Count");
        System.out.println("--------------------------------------");

        // Find the best performing buffer size (lowest average time)
        Map<String, Object> bestResult = null;

        for (Map<String, Object> result : benchmarkResults) {
            int bufferSize = (int) result.get("bufferSize");
            double avgTime = (double) result.get("averageTimeMs");
            double avgCount = (double) result.get("averageCount");

            System.out.printf("%-12d %-15.2f %-15.2f%n", bufferSize, avgTime, avgCount);

            if (bestResult == null || (double) result.get("averageTimeMs") < (double) bestResult.get("averageTimeMs")) {
                bestResult = result;
            }
        }

        if (bestResult != null) {
            System.out.println("\nOptimal buffer size for your system: " + bestResult.get("bufferSize") + 
                " (Average time: " + String.format("%.2f", (double) bestResult.get("averageTimeMs")) + " ms)");
        }
    }
}
