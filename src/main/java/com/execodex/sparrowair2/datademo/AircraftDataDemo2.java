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

//this should be loaded from the file
public class AircraftDataDemo {

    public static Flux<Aircraft> getDemoAircraftsFromFile() {
        // Create a ClassPathResource for the JSON file
        ClassPathResource resource = new ClassPathResource("stuff/data/iaka.jsonl");

        //TODO: this needs refactoring
//        ResolvableType targetType = ResolvableType.forClassWithGenerics(Flux.class, MyJson.class);

//        return decoder.decode(
//                dataBufferFlux,
//                targetType,
//                MimeType.valueOf("application/json"),
//                null
//        );
        // Create a Jackson2JsonDecoder for parsing JSON
        Jackson2JsonDecoder decoder = new Jackson2JsonDecoder(new ObjectMapper());
//        ResolvableType targetType = ResolvableType.forClassWithGenerics(List.class, Aircraft.class);
        ResolvableType targetType = ResolvableType.forClass(Aircraft.class);

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
        Flux<Aircraft> aircraftFlux = dataBufferFlux.flatMap(dataBuffer -> decoder.decode(
                                Flux.just(dataBuffer),
                                targetType,
                                MimeTypeUtils.APPLICATION_JSON,
                                null)
                        .map(Aircraft.class::cast))
                .onErrorResume(e -> {
                    System.err.println("Error reading aircraft data: " + e.getMessage());
                    return Flux.empty();
                });
        ;


//        Flux<Aircraft> map = decoder.decode(
//                        dataBufferFlux,
//                        targetType,
//null,
//                        MimeTypeUtils.APPLICATION_JSON,
//                        null
//                )
//                .flatMapIterable(obj -> (List<Aircraft>) obj)
//                .map(List.class::cast)
//                .flatMap(obj -> {
//                     Ensure the object is a List<Aircraft> before casting
//                    if (obj instanceof List) {
//                        return Flux.fromIterable((List<Aircraft>) obj);
//                    } else {
//                        return Flux.error(new IllegalArgumentException("Decoded object is not a List<Aircraft>"));
//                    }
//                })
//                .flatMap(obj -> Flux.fromIterable((List<Aircraft>) obj))
//                .onErrorResume(e -> {
                    // Log the error and return an empty Flux
//                    System.err.println("Error reading aircraft data: " + e.getMessage());
//                    return Flux.empty();
//                });
        return aircraftFlux;

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
