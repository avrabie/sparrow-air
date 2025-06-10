package com.execodex.sparrowair2.datademo;

import com.execodex.sparrowair2.datademo.kaggle.AirlineDataDemo;
import com.execodex.sparrowair2.entities.kaggle.AirlineNew;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for AirlineDataDemo.
 * Tests the functionality of loading AirlineNew entities from a CSV file.
 */
public class AirlineDataDemoTest {

    @Test
    public void testLoadAirlinesFromFile() {
        // Path to the test data file
        String dataPath = "stuff/data/airlines/airlines.csv";
        
        // Get the Flux of AirlineNew objects
        Flux<AirlineNew> airlinesFlux = AirlineDataDemo.getDemoAirlinesFromFile(dataPath);
        
        // Convert to a list for easier testing
        List<AirlineNew> airlines = airlinesFlux.collectList().block();
        
        // Verify that we loaded some airlines
        assertNotNull(airlines, "Airlines list should not be null");
        assertFalse(airlines.isEmpty(), "Airlines list should not be empty");
        
        System.out.println("Loaded " + airlines.size() + " airlines from " + dataPath);
        
        // Verify some expected data
        // Find a specific airline by ID (e.g., American Airlines with ID 24)
        AirlineNew americanAirlines = airlines.stream()
                .filter(airline -> airline.getAirlineId() != null && airline.getAirlineId() == 24)
                .findFirst()
                .orElse(null);
        
        assertNotNull(americanAirlines, "Should find American Airlines");
        assertEquals("American Airlines", americanAirlines.getName(), "Name should match");
        assertEquals("AA", americanAirlines.getIata(), "IATA code should match");
        assertEquals("AAL", americanAirlines.getIcaoCode(), "ICAO code should match");
        assertEquals("AMERICAN", americanAirlines.getCallsign(), "Callsign should match");
        assertEquals("United States", americanAirlines.getCountry(), "Country should match");
        assertEquals("Y", americanAirlines.getActive(), "Active status should match");
    }
    
    @Test
    public void testLoadAirlinesFromFileReactive() {
        // Path to the test data file
        String dataPath = "stuff/data/airlines/airlines.csv";
        
        // Get the Flux of AirlineNew objects
        Flux<AirlineNew> airlinesFlux = AirlineDataDemo.getDemoAirlinesFromFile(dataPath);
        
        // Use StepVerifier to test the reactive stream
        StepVerifier.create(airlinesFlux)
                .expectNextCount(1) // Expect at least one element
                .thenConsumeWhile(airline -> true) // Consume all elements
                .verifyComplete(); // Verify the stream completes without errors
    }
}