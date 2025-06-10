package com.execodex.sparrowair2.datademo.skybrary;

import com.execodex.sparrowair2.entities.skybrary.Aircraft;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.channels.AsynchronousFileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This class demonstrates non-blocking reading of aircraft data from a JSONL file.
 * It processes the file in chunks, emitting data up to and including newlines.
 * It also provides benchmarking capabilities to test different buffer sizes.
 */
public class AircraftDataDemo {

    // Default buffer size
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    /**
     * Get demo aircrafts from file using the default buffer size (8192)
     * @param path Path to the JSONL file
     * @return Flux of Aircraft objects
     */
    public static Flux<Aircraft> getDemoAircraftsFromFile(String path) {
        return getDemoAircraftsFromFile(path, DEFAULT_BUFFER_SIZE);
    }

    /**
     * Get demo aircrafts from file with a specified buffer size
     * @param path Path to the JSONL file
     * @param bufferSize Buffer size to use for reading
     * @return Flux of Aircraft objects
     */
    public static Flux<Aircraft> getDemoAircraftsFromFile(String path, int bufferSize) {
        // Create a ClassPathResource for the JSON file
        ClassPathResource resource = new ClassPathResource(path);
        ObjectMapper mapper = new ObjectMapper();

        // This will hold any leftover bytes from the previous buffer that didn't contain a newline
        AtomicReference<String> leftover = new AtomicReference<>("");

        return Mono.fromCallable(() -> {
            try {
                return AsynchronousFileChannel.open(
                        resource.getFile().toPath(),
                        StandardOpenOption.READ
                );
            } catch (Exception e) {
                throw new RuntimeException("Error opening file: " + e.getMessage(), e);
            }
        })
        .flatMapMany(channel -> 
            DataBufferUtils.readAsynchronousFileChannel(
                () -> channel,
                DefaultDataBufferFactory.sharedInstance,
                bufferSize
            )
            .concatMap(buffer -> {
                // Convert buffer to string
                byte[] bytes = new byte[buffer.readableByteCount()];
                buffer.read(bytes);
                DataBufferUtils.release(buffer);
                String content = leftover.getAndSet("") + new String(bytes, StandardCharsets.UTF_8);

                // Process the content to extract complete lines
                List<String> lines = new ArrayList<>();
                int lastNewlineIndex = content.lastIndexOf('\n');

                if (lastNewlineIndex >= 0) {
                    // We have at least one complete line
                    String completeLines = content.substring(0, lastNewlineIndex + 1);

                    // Store any remaining content for the next buffer
                    if (lastNewlineIndex < content.length() - 1) {
                        leftover.set(content.substring(lastNewlineIndex + 1));
                    }

                    // Split complete lines and add to our list
                    for (String line : completeLines.split("\n")) {
                        if (!line.trim().isEmpty()) {
                            lines.add(line);
                        }
                    }
                } else {
                    // No newline found, add all content to leftover
                    leftover.set(content);
                }

                return Flux.fromIterable(lines);
            })
            // Process any remaining content after all buffers are read
            .concatWith(Mono.fromSupplier(() -> {
                String remaining = leftover.getAndSet("");
                if (!remaining.trim().isEmpty()) {
                    return remaining;
                }
                return null;
            }).filter(Objects::nonNull).flux())
            // Parse each line into an Aircraft object
            .map(line -> {
                if (line == null || line.trim().isEmpty()) {
                    return null;
                }
                try {
                    return mapper.readValue(line, Aircraft.class);
                } catch (JsonProcessingException e) {
                    // Only log errors for non-empty lines
                    if (!line.trim().isEmpty()) {
                        System.err.println("Error parsing JSON line: " + e.getMessage());
                    }
                    return null;
                }
            })
            .filter(Objects::nonNull)
        )
        .onErrorResume(e -> {
            // Only log errors that are not related to empty content
            if (e.getMessage() == null || !e.getMessage().contains("No content to map due to end-of-input")) {
                System.err.println("Error reading aircraft data: " + e.getMessage());
            }
            return Flux.empty();
        });
    }

    /**
     * Benchmark the performance of reading aircraft data with a specific buffer size
     * @param path Path to the JSONL file
     * @param bufferSize Buffer size to test
     * @param iterations Number of iterations to run for more accurate results
     * @return Map containing performance metrics (time in milliseconds, count of items)
     */
    public static Map<String, Object> benchmarkBufferSize(String path, int bufferSize, int iterations) {
        Map<String, Object> results = new HashMap<>();
        results.put("bufferSize", bufferSize);

        long totalTime = 0;
        long totalCount = 0;

        for (int i = 0; i < iterations; i++) {
            long startTime = System.nanoTime();

            // Count the number of items processed
            long count = getDemoAircraftsFromFile(path, bufferSize)
                .count()
                .block();

            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1_000_000; // Convert to milliseconds

            totalTime += duration;
            totalCount += count;

            // Add a small delay between iterations
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Calculate averages
        double avgTime = (double) totalTime / iterations;
        double avgCount = (double) totalCount / iterations;

        results.put("averageTimeMs", avgTime);
        results.put("averageCount", avgCount);
        results.put("iterations", iterations);

        return results;
    }

    /**
     * Run benchmarks for multiple buffer sizes and return the results
     * @param path Path to the JSONL file
     * @param bufferSizes Array of buffer sizes to test
     * @param iterations Number of iterations for each buffer size
     * @return List of maps containing performance metrics for each buffer size
     */
    public static List<Map<String, Object>> runBufferSizeBenchmarks(String path, int[] bufferSizes, int iterations) {
        List<Map<String, Object>> results = new ArrayList<>();

        for (int bufferSize : bufferSizes) {
            System.out.println("Benchmarking buffer size: " + bufferSize);
            Map<String, Object> result = benchmarkBufferSize(path, bufferSize, iterations);
            results.add(result);
        }

        return results;
    }

    /**
     * Main method to run benchmarks and display results
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        String dataPath = "stuff/data/iaka2.jsonl";
        int[] bufferSizes = {1024, 2048, 4096, 8192, 16384, 32768, 65536};
        int iterations = 5;

        System.out.println("Starting buffer size benchmark...");
        List<Map<String, Object>> benchmarkResults = runBufferSizeBenchmarks(dataPath, bufferSizes, iterations);

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
