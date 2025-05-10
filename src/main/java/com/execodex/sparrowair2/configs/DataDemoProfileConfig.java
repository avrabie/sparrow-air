package com.execodex.sparrowair2.configs;

import com.execodex.sparrowair2.entities.AircraftType;
import com.execodex.sparrowair2.entities.Airport;
import com.execodex.sparrowair2.services.AircraftTypeService;
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
    public CommandLineRunner initializeAirportData(AirportService airportService, AircraftTypeService aircraftTypeService) {
        return args -> {
            logger.info("Initializing sample airport data for 'datademo' profile");

            // Insert sample airports and aircraft types into the database
            generateAirport(airportService)
                    .thenMany(generateAircraftType(aircraftTypeService))
                    .doOnComplete(() -> logger.info("Sample airport data initialization completed"))
                    .blockLast();
        };
    }

    /**
     * Generates sample airports and inserts them into the database if they do not already exist.
     *
     * @param airportService The service to interact with airport data.
     * @return A Flux of generated Airport objects.
     */
    @Bean(name = "airportDataGenerator")
    public Flux<Airport> generateAirport(AirportService airportService) {
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

        // Insert sample airports into the database
        Flux<Airport> airportFlux = Flux.fromIterable(sampleAirports)
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
                        .onErrorResume(e -> {
                            logger.warn("Could not create airport {}: {}", airport.getIcaoCode(), e.getMessage());
                            return Mono.empty();
                        })
                );
        return airportFlux;
    }
    /**
     * Generates sample aircraft types and inserts them into the database if they do not already exist.
     *
     * @param aircraftTypeService The service to interact with aircraft type data.
     * @return A Flux of generated AircraftType objects.
     */

    @Bean(name = "aircraftTypeDataGenerator")
    public Flux<AircraftType> generateAircraftType(AircraftTypeService aircraftTypeService){
        List<AircraftType> sampleAircraftTypes = Arrays.asList(
                AircraftType.builder()
                        .icaoCode("B738")
                        .modelName("737-800")
                        .manufacturer("Boeing")
                        .seatingCapacity(189)
                        .maxRangeKm(5765)
                        .mtow(79010)
                        .build(),
                AircraftType.builder()
                        .icaoCode("A320")
                        .modelName("A320-200")
                        .manufacturer("Airbus")
                        .seatingCapacity(180)
                        .maxRangeKm(6100)
                        .mtow(77000)
                        .build(),
                AircraftType.builder()
                        .icaoCode("B77W")
                        .modelName("777-300ER")
                        .manufacturer("Boeing")
                        .seatingCapacity(396)
                        .maxRangeKm(13650)
                        .mtow(351500)
                        .build(),
                AircraftType.builder()
                        .icaoCode("A388")
                        .modelName("A380-800")
                        .manufacturer("Airbus")
                        .seatingCapacity(853)
                        .maxRangeKm(15700)
                        .mtow(575000)
                        .build(),
                AircraftType.builder()
                        .icaoCode("E190")
                        .modelName("E190")
                        .manufacturer("Embraer")
                        .seatingCapacity(114)
                        .maxRangeKm(4537)
                        .mtow(51800)
                        .build()
        );

        // Insert sample aircraft types into the database
        Flux<AircraftType> aircraftTypeFlux = Flux.fromIterable(sampleAircraftTypes)
                .flatMap(aircraftType -> aircraftTypeService
                        .getAircraftTypeByIcaoCode(aircraftType.getIcaoCode())
                        .hasElement()
                        .flatMap(existingAircraftType -> {
                            if (existingAircraftType) {
                                logger.info("Aircraft type {} already exists, skipping creation", aircraftType.getIcaoCode());
                                return Mono.empty();
                            }
                            return aircraftTypeService.createAircraftType(aircraftType);
                        })
                        .onErrorResume(e -> {
                            logger.warn("Could not create aircraft type {}: {}", aircraftType.getIcaoCode(), e.getMessage());
                            return Mono.empty();
                        })
                );
        return aircraftTypeFlux;
    }
}
