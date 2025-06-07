package com.execodex.sparrowair2.datademo;

import com.execodex.sparrowair2.entities.Aircraft;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

//this should be loaded from the file
public class AircraftDataDemo {

    public static Flux<Aircraft> getDemoAircraftsFromFile() {
        // Create a ClassPathResource for the JSON file
        ClassPathResource resource = new ClassPathResource("stuff/data/iaka.jsonl");
        // Create a Jackson2JsonDecoder for parsing JSON
        ObjectMapper mapper = new ObjectMapper();
        Jackson2JsonDecoder decoder = new Jackson2JsonDecoder();
//        ResolvableType targetType = ResolvableType.forClassWithGenerics(List.class, Aircraft.class);
        ResolvableType targetType = ResolvableType.forClass(Aircraft.class);


        Flux<DataBuffer> dataBufferFlux2 = DataBufferUtils
                .readAsynchronousFileChannel(() -> AsynchronousFileChannel.open(resource.getFile().toPath(), StandardOpenOption.READ),
                        DefaultDataBufferFactory.sharedInstance, 8192);

        // Use DataBufferUtils to read the file as a reactive stream
        Flux<DataBuffer> dataBufferFlux = DataBufferUtils.readInputStream(
                () -> {
                    try {
                        return resource.getInputStream();
                    } catch (IOException e) {
                        throw new RuntimeException("Error reading resource: " + e.getMessage(), e);
                    }
                },
                DefaultDataBufferFactory.sharedInstance,
                8192);
        Flux<Aircraft> aircraftFlux = dataBufferFlux2.flatMap(dataBuffer -> decoder.decode(
                                Flux.just(dataBuffer),
                                targetType,
                                MimeTypeUtils.APPLICATION_JSON,
                                null)
                        .map(Aircraft.class::cast))
                .onErrorResume(e -> {
                    System.err.println("Error reading aircraft data: " + e.getMessage());
                    return Flux.empty();
                });

//        Flux<Aircraft> aircraftFlux = dataBufferFlux
//                .map(dataBuffer -> {
//                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
//                    dataBuffer.read(bytes);
//                    DataBufferUtils.release(dataBuffer);
//                    return new String(bytes, StandardCharsets.UTF_8);
//                })
//                .flatMap(content -> Flux.fromArray(content.split("\n")))
//                .filter(line -> !line.trim().isEmpty())
//                .map(line -> {
//                    try {
//                        return mapper.readValue(line, Aircraft.class);
//                    } catch (JsonProcessingException e) {
//                        System.err.println("Error parsing JSON line: " + line + e);
//                        return null;
//                    }
//                })


        return aircraftFlux
                .filter(Objects::nonNull)
                .onErrorResume(e -> {
                    System.err.println("Error reading aircraft data: " + e.getMessage());
                    return Flux.empty();
                });


//                .collectList()
//                .flatMapMany(dataBuffers -> {
//                     Decode the JSON data into a Flux of Aircraft objects
//                    return decoder.decode(
//                            Flux.fromIterable(dataBuffers),
//                            ResolvableType.forClassWithGenerics(List.class, Aircraft.class),
//                            MimeTypeUtils.APPLICATION_JSON,
//                            null
//                    );
//                })
//                .flatMap(obj -> Flux.fromIterable((List<Aircraft>) obj))
//                .onErrorResume(e -> {
//                     Log the error and return an empty Flux
//                    System.err.println("Error reading aircraft data: " + e.getMessage());
//                    return Flux.empty();
//                }
//                );
    }

}
