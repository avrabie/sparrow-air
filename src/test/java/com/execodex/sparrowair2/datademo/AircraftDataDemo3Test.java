package com.execodex.sparrowair2.datademo;

import com.execodex.sparrowair2.entities.Aircraft;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

public class AircraftDataDemo3Test {

    @Test
    public void testGetDemoAircraftsFromFile() {
        // Get the Flux of Aircraft objects from AircraftDataDemo3
        Flux<Aircraft> aircraftFlux = AircraftDataDemo3.getDemoAircraftsFromFile("stuff/data/iaka.jsonl");

        // Collect the aircraft into a list and print some information
        List<Aircraft> aircraftList = aircraftFlux.collectList().block(Duration.ofSeconds(10));

        System.out.println("[DEBUG_LOG] Number of aircraft from AircraftDataDemo3: " + aircraftList.size());
        if (!aircraftList.isEmpty()) {
            Aircraft firstAircraft = aircraftList.get(0);
            System.out.println("[DEBUG_LOG] First aircraft: " + firstAircraft.getIcaoCode() + " - " + firstAircraft.getName());
        }

        // Simple assertion to verify that we got some aircraft
        assert aircraftList != null && !aircraftList.isEmpty() : "No aircraft were loaded";
    }

    @Test
    public void testCompareWithAircraftDataDemo2() {
        // Get the Flux of Aircraft objects from both implementations
        Flux<Aircraft> aircraftFlux2 = AircraftDataDemo2.getDemoAircraftsFromFile("stuff/data/iaka.jsonl");
        Flux<Aircraft> aircraftFlux3 = AircraftDataDemo3.getDemoAircraftsFromFile("stuff/data/iaka.jsonl");

        // Collect the aircraft into lists
        List<Aircraft> aircraftList2 = aircraftFlux2.collectList().block(Duration.ofSeconds(10));
        List<Aircraft> aircraftList3 = aircraftFlux3.collectList().block(Duration.ofSeconds(10));

        // Print the counts for debugging
        System.out.println("[DEBUG_LOG] AircraftDataDemo2 count: " + (aircraftList2 != null ? aircraftList2.size() : 0));
        System.out.println("[DEBUG_LOG] AircraftDataDemo3 count: " + (aircraftList3 != null ? aircraftList3.size() : 0));

        // Simple assertion to verify that both implementations return the same number of aircraft
        assert aircraftList2 != null && aircraftList3 != null : "One of the aircraft lists is null";
        assert aircraftList2.size() == aircraftList3.size() : 
            "Different number of aircraft: AircraftDataDemo2=" + aircraftList2.size() + 
            ", AircraftDataDemo3=" + aircraftList3.size();
    }
}
