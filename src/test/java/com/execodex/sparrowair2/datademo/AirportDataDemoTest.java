package com.execodex.sparrowair2.datademo;

import com.execodex.sparrowair2.datademo.skybrary.AirportDataDemo;
import com.execodex.sparrowair2.entities.skybrary.AirportNew;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for AirportDataDemo.
 * Tests the functionality of loading AirportNew entities from a JSONL file.
 */
public class AirportDataDemoTest {

    @Test
    public void testLoadAirportsFromFile() {
        // Path to the test data file
        String dataPath = "stuff/data/airports/iaka_airports.jsonl";
        
        // Get the Flux of AirportNew objects
        Flux<AirportNew> airportsFlux = AirportDataDemo.getDemoAirportsFromFile(dataPath);
        
        // Convert to a list for easier testing
        List<AirportNew> airports = airportsFlux.collectList().block();
        
        // Verify that we loaded some airports
        assertNotNull(airports, "Airports list should not be null");
        assertFalse(airports.isEmpty(), "Airports list should not be empty");
        
        System.out.println("Loaded " + airports.size() + " airports from " + dataPath);
        
        // Verify some expected data
        // Find a specific airport by ICAO code (e.g., KXTA - Area 51)
        AirportNew area51 = airports.stream()
                .filter(airport -> "KXTA".equals(airport.getIcaoCode()))
                .findFirst()
                .orElse(null);
        
        assertNotNull(area51, "Should find Area 51 airport");
        assertEquals("XTA", area51.getIataCode(), "IATA code should match");
        assertEquals("Area 51", area51.getName(), "Name should match");
        assertEquals("United States", area51.getCountry(), "Country should match");
        assertEquals(37.23777777777778, area51.getLatitude(), 0.0001, "Latitude should match");
        assertEquals(-115.79916666666666, area51.getLongitude(), 0.0001, "Longitude should match");
    }
    
    @Test
    public void testLoadAirportsFromFileReactive() {
        // Path to the test data file
        String dataPath = "stuff/data/airports/iaka_airports.jsonl";
        
        // Get the Flux of AirportNew objects
        Flux<AirportNew> airportsFlux = AirportDataDemo.getDemoAirportsFromFile(dataPath);
        
        // Use StepVerifier to test the reactive stream
        StepVerifier.create(airportsFlux)
                .expectNextCount(1) // Expect at least one element
                .thenConsumeWhile(airport -> true) // Consume all elements
                .verifyComplete(); // Verify the stream completes without errors
    }
}