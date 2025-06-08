package com.execodex.sparrowair2.configs;

import com.execodex.sparrowair2.entities.Aircraft;
import com.execodex.sparrowair2.entities.Airline;
import com.execodex.sparrowair2.entities.Airport;
import com.execodex.sparrowair2.repositories.AircraftRepository;
import com.execodex.sparrowair2.repositories.AirlineRepository;
import com.execodex.sparrowair2.repositories.AirportRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@ActiveProfiles("datademo")
@Testcontainers
public class DataDemoProfileConfigTest extends AbstractTestcontainersTest {

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private AircraftRepository aircraftRepository;

    @Autowired
    private AirlineRepository airlineRepository;

    @Test
    public void testSampleAirportsAreCreated() {
        // List of ICAO codes that should be created by the datademo profile
        List<String> expectedIcaoCodes = Arrays.asList("KJFK", "EGLL", "RJTT", "YSSY", "EDDF");

        // Fetch all airports from the repository
        Flux<Airport> airports = airportRepository.findAll();

        // Verify that all expected airports exist
        StepVerifier.create(
                        airports.map(Airport::getIcaoCode)
                                .filter(expectedIcaoCodes::contains)
                                .collectList()
                                .map(foundCodes -> {
                                    System.out.println("[DEBUG_LOG] Found airports: " + foundCodes);
                                    return foundCodes.size() == expectedIcaoCodes.size();
                                })
                )
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    public void testSampleAircraftTypesAreCreated() {
        // List of ICAO codes that should be created by the datademo profile
        List<String> expectedIcaoCodes = Arrays.asList("B738", "A320", "B77W", "A388", "E190");

        // Fetch all aircraft types from the repository
        Flux<Aircraft> aircraftTypes = aircraftRepository.findAll();

        // Verify that all expected aircraft types exist
        StepVerifier.create(
                        aircraftTypes.map(Aircraft::getIcaoCode)
                                .filter(expectedIcaoCodes::contains)
                                .collectList()
                                .map(foundCodes -> {
                                    System.out.println("[DEBUG_LOG] Found aircraft types: " + foundCodes);
                                    return foundCodes.size() == expectedIcaoCodes.size();
                                })
                )
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    public void testSampleAirlinesAreCreated() {
        // List of ICAO codes that should be created by the datademo profile
        List<String> expectedIcaoCodes = Arrays.asList("AAL", "BAW", "DLH", "UAE", "SIA");

        // Fetch all airlines from the repository
        Flux<Airline> airlines = airlineRepository.findAll();

        // Verify that all expected airlines exist
        StepVerifier.create(
                        airlines.map(Airline::getIcaoCode)
                                .filter(expectedIcaoCodes::contains)
                                .collectList()
                                .map(foundCodes -> {
                                    System.out.println("[DEBUG_LOG] Found airlines: " + foundCodes);
                                    return foundCodes.size() == expectedIcaoCodes.size();
                                })
                )
                .expectNext(true)
                .verifyComplete();
    }
}
