package com.execodex.sparrowair2.datademo;

import com.execodex.sparrowair2.entities.Aircraft;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.core.ResolvableType;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.List;

//this should be loaded from the file
public class AircraftDataDemo {

    public static Flux<Aircraft> getDemoAircraftsFromFile() {
        // Create a ClassPathResource for the JSON file
        ClassPathResource resource = new ClassPathResource("stuff/data/sample-aircraft.json");

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

        // Use DataBufferUtils to read the file as a reactive stream
        return DataBufferUtils.readInputStream(
                () -> {
                    try {
                        return resource.getInputStream();
                    } catch (IOException e) {
                        throw new RuntimeException("Error reading resource: " + e.getMessage(), e);
                    }
                },
                DefaultDataBufferFactory.sharedInstance,
                4096)
                .collectList()
                .flatMapMany(dataBuffers -> {
                    // Decode the JSON data into a Flux of Aircraft objects
                    return decoder.decode(
                            Flux.fromIterable(dataBuffers),
                            ResolvableType.forClassWithGenerics(List.class, Aircraft.class),
                            MimeTypeUtils.APPLICATION_JSON,
                            null
                    );
                })
                .flatMap(obj -> Flux.fromIterable((List<Aircraft>) obj))
                .onErrorResume(e -> {
                    // Log the error and return an empty Flux
                    System.err.println("Error reading aircraft data: " + e.getMessage());
                    return Flux.empty();
                });
    }

}
