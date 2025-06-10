package com.execodex.sparrowair2.datademo.skybrary;

import com.execodex.sparrowair2.entities.skybrary.AirportNew;
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
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This class demonstrates non-blocking reading of airport data from a JSONL file.
 * It processes the file in chunks, emitting data up to and including newlines.
 */
public class AirportDataDemo {

    // Default buffer size
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    /**
     * Get demo airports from file using the default buffer size (8192)
     * @param path Path to the JSONL file
     * @return Flux of AirportNew objects
     */
    public static Flux<AirportNew> getDemoAirportsFromFile(String path) {
        return getDemoAirportsFromFile(path, DEFAULT_BUFFER_SIZE);
    }

    /**
     * Get demo airports from file with a specified buffer size
     * @param path Path to the JSONL file
     * @param bufferSize Buffer size to use for reading
     * @return Flux of AirportNew objects
     */
    public static Flux<AirportNew> getDemoAirportsFromFile(String path, int bufferSize) {
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
            // Parse each line into an AirportNew object
            .map(line -> {
                if (line == null || line.trim().isEmpty()) {
                    return null;
                }
                try {
                    return mapper.readValue(line, AirportNew.class);
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
                System.err.println("Error reading airport data: " + e.getMessage());
            }
            return Flux.empty();
        });
    }
}