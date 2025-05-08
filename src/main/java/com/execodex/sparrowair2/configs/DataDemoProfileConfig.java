package com.execodex.sparrowair2.configs;

import com.execodex.sparrowair2.entities.Airport;
import com.execodex.sparrowair2.services.AirportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Configuration
@Profile("datademo")
public class DataDemoProfileConfig {

    private static final Logger logger = LoggerFactory.getLogger(DataDemoProfileConfig.class);

    @Bean
    public CommandLineRunner initializeSampleData(AirportService airportService) {
        return args -> {
            logger.info("Initializing sample airport data for 'datademo' profile");

            List<Airport> sampleAirports = Arrays.asList(
                    Airport.builder()
                            .icaoCode("KJFK")
                            .name("John F. Kennedy International Airport")
                            .city("New York")
                            .country("United States")
                            .timezone("America/New_York")
                            .latitude(40.6413)
                            .longitude(-73.7781)
                            .build(),
                    Airport.builder()
                            .icaoCode("EGLL")
                            .name("London Heathrow Airport")
                            .city("London")
                            .country("United Kingdom")
                            .timezone("Europe/London")
                            .latitude(51.4700)
                            .longitude(-0.4543)
                            .build(),
                    Airport.builder()
                            .icaoCode("RJTT")
                            .name("Tokyo Haneda Airport")
                            .city("Tokyo")
                            .country("Japan")
                            .timezone("Asia/Tokyo")
                            .latitude(35.5494)
                            .longitude(139.7798)
                            .build(),
                    Airport.builder()
                            .icaoCode("YSSY")
                            .name("Sydney Kingsford Smith Airport")
                            .city("Sydney")
                            .country("Australia")
                            .timezone("Australia/Sydney")
                            .latitude(-33.9399)
                            .longitude(151.1753)
                            .build(),
                    Airport.builder()
                            .icaoCode("EDDF")
                            .name("Frankfurt Airport")
                            .city("Frankfurt")
                            .country("Germany")
                            .timezone("Europe/Berlin")
                            .latitude(50.0379)
                            .longitude(8.5622)
                            .build()
            );

            // Insert sample airports
//            Mono<List<String>> listMono = airportService.getAllAirports()
//                    .map(Airport::getIcaoCode)
//                    .collectList();
            Flux.fromIterable(sampleAirports)
                    .flatMap(airport -> airportService
                            .getAirportByIcaoCode(airport.getIcaoCode())
                            .hasElement()
                            .flatMap(existingAirport -> {
                                if (existingAirport) {
                                    logger.info("Airport {} already exists, skipping creation", airport.getIcaoCode());
                                    return Mono.empty();
                                }
                                return airportService.createAirport(airport);
                            })
//                            .switchIfEmpty(airportService.createAirport(airport))
                            .onErrorResume(e -> {
                                logger.warn("Could not create airport {}: {}", airport.getIcaoCode(), e.getMessage());
                                return Mono.empty();
                            })
                    )
                    .doOnComplete(() -> logger.info("Sample airport data initialization completed"))
                    .blockLast();


//            Flux.fromIterable(sampleAirports)
//                    .flatMap(airport -> airportService.createAirport(airport)
//                            .onErrorResume(e -> {
//                                logger.warn("Could not create airport {}: {}", airport.getIcaoCode(), e.getMessage());
//                                return Mono.empty();
//                            })
//                    )
//                    .doOnComplete(() -> logger.info("Sample airport data initialization completed"))
//                    .blockLast(); // Block until all operations complete
        };
    }
}