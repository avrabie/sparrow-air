package com.execodex.sparrowair2.datademo.kaggle;

import com.execodex.sparrowair2.entities.kaggle.AirlineNew;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.channels.AsynchronousFileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This class demonstrates non-blocking reading of airline data from a CSV file.
 * It processes the file in chunks, emitting data up to and including newlines.
 */
public class AirlineDataDemo {

    // Default buffer size
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    /**
     * Get demo airlines from file using the default buffer size (8192)
     * @param path Path to the CSV file
     * @return Flux of AirlineNew objects
     */
    public static Flux<AirlineNew> getDemoAirlinesFromFile(String path) {
        return getDemoAirlinesFromFile(path, DEFAULT_BUFFER_SIZE);
    }

    /**
     * Get demo airlines from file with a specified buffer size
     * @param path Path to the CSV file
     * @param bufferSize Buffer size to use for reading
     * @return Flux of AirlineNew objects
     */
    public static Flux<AirlineNew> getDemoAirlinesFromFile(String path, int bufferSize) {
        // Create a ClassPathResource for the CSV file
        ClassPathResource resource = new ClassPathResource(path);

        // This will hold any leftover bytes from the previous buffer that didn't contain a newline
        AtomicReference<String> leftover = new AtomicReference<>("");

        // Flag to skip the header row
        AtomicReference<Boolean> headerSkipped = new AtomicReference<>(false);

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
            // Parse each line into an AirlineNew object
            .map(line -> {
                if (line == null || line.trim().isEmpty()) {
                    return null;
                }

                // Skip the header row
                if (!headerSkipped.get()) {
                    headerSkipped.set(true);
                    return null;
                }

                try {
                    // Parse CSV line into AirlineNew object
                    return parseAirlineFromCsvLine(line);
                } catch (Exception e) {
                    // Only log errors for non-empty lines
                    if (!line.trim().isEmpty()) {
                        System.err.println("Error parsing CSV line: " + e.getMessage());
                    }
                    return null;
                }
            })
            .filter(Objects::nonNull)
        )
        .onErrorContinue((e, obj) -> {
            System.err.println("Error reading airline data: " + e.getMessage());
        });
    }

    /**
     * Parse a CSV line into an AirlineNew object
     * @param line CSV line to parse
     * @return AirlineNew object
     */
    private static AirlineNew parseAirlineFromCsvLine(String line) {
        // Split the CSV line by comma
        // This is a simple implementation and doesn't handle quoted fields with commas
        String[] parts = line.split(",");

        if (parts.length < 8) {
            throw new IllegalArgumentException("CSV line does not have enough fields: " + line);
        }

        // Parse the airline ID
        Integer airlineId = null;
        try {
            String idStr = parts[0].trim();
            if (!idStr.equals("\\N") && !idStr.isEmpty()) {
                airlineId = Integer.parseInt(idStr);
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid airline ID: " + parts[0]);
        }

        // Create and return the AirlineNew object
        return AirlineNew.builder()
                .airlineId(airlineId)
                .name(normalizeField(parts[1]))
                .alias(normalizeField(parts[2]))
                .iata(normalizeField(parts[3]))
                .icaoCode(normalizeField(parts[4]))
                .callsign(normalizeField(parts[5]))
                .country(normalizeField(parts[6]))
                .active(normalizeField(parts[7]))
                .build();
    }

    /**
     * Normalize a field value by converting "\N" to null and trimming
     * @param value Field value to normalize
     * @return Normalized field value
     */
    private static String normalizeField(String value) {
        if (value == null || value.trim().isEmpty() || value.equals("\\N")) {
            return null;
        }
        return value.trim();
    }
}
