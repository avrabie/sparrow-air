package com.execodex.sparrowair2.configs;

import com.execodex.sparrowair2.entities.Airport;
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
}
