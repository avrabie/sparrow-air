package com.execodex.sparrowair2.datademo;

import com.execodex.sparrowair2.entities.Aircraft;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import reactor.core.publisher.Flux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

//this should be loaded from the file
public class AircraftDataDemo2 {

    public static Flux<Aircraft> getDemoAircraftsFromFile(String path) {
        // Create a ClassPathResource for the JSON file
        ClassPathResource resource = new ClassPathResource(path);

        ObjectMapper mapper = new ObjectMapper();
        Jackson2JsonDecoder decoder = new Jackson2JsonDecoder(mapper);
        ResolvableType targetType = ResolvableType.forClass(Aircraft.class);

        Flux<Aircraft> map = Flux.using(
                () -> new BufferedReader(new InputStreamReader(resource.getInputStream())),
                reader ->
                        Flux.fromStream(reader.lines())
                                .map(line -> {
                                    try {
                                        return mapper.readValue(line, Aircraft.class);
                                    } catch (IOException e) {
                                        throw new RuntimeException("Error parsing line: " + e.getMessage(), e);
                                    }
                                })
                                .filter(Objects::nonNull),
                reader -> {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        throw new RuntimeException("Error closing reader: " + e.getMessage(), e);
                    }

                })
                .onErrorResume(e -> {;
                    System.err.println("Error reading aircraft data: " + e.getMessage());
                    return Flux.empty();
                });
        return map;

    }

}
