package com.execodex.sparrowair2.services;

import com.execodex.sparrowair2.entities.Airport;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AirportNew2ServiceTest {

    @Test
    void testDistanceBetweenTwoAirports_ShortDistance() {
        Airport airport21 = Airport.builder()
                .icaoCode("JFK")
                .latitude(40.6413)
                .longitude(-73.7781)
                .build();

        Airport airport = Airport.builder()
                .icaoCode("LGA")
                .latitude(40.7769)
                .longitude(-73.8740)
                .build();

        AirportService airportService = new AirportService(null);

        double distance = airportService.distance(airport21, airport);

        assertTrue(distance > 0);
        assertEquals(17, distance, 1.0);
    }

    @Test
    void testDistanceBetweenTwoAirports_LongDistance() {
        Airport airport21 = Airport.builder()
                .icaoCode("LAX")
                .latitude(33.9416)
                .longitude(-118.4085)
                .build();

        Airport airport = Airport.builder()
                .icaoCode("HND")
                .latitude(35.5494)
                .longitude(139.7798)
                .build();

        AirportService airportService = new AirportService(null);

        double distance = airportService.distance(airport21, airport);

        assertTrue(distance > 0);
        assertEquals(8799.8, distance, 50.0);
    }

    @Test
    void testDistanceBetweenSameAirport() {
        Airport airport = Airport.builder()
                .icaoCode("ORD")
                .latitude(41.9742)
                .longitude(-87.9073)
                .build();

        AirportService airportService = new AirportService(null);

        double distance = airportService.distance(airport, airport);

        assertEquals(0.0, distance, 0.01);
    }

    // chisinau 46.9352° N, 28.9349° E
    // basel 47.5596° N, 7.5886° E
    @Test
    void testDistanceBetweenTwoAirports_ZeroDistance() {
        Airport airport21 = Airport.builder()
                .icaoCode("KIV")
                .latitude(46.9352)
                .longitude(28.9349)
                .build();

        Airport airport = Airport.builder()
                .icaoCode("BSL")
                .latitude(47.5596)
                .longitude(7.5886)
                .build();


        AirportService airportService = new AirportService(null);
        double distance = airportService.distance(airport21, airport);
        assertTrue(distance > 0);
        assertEquals(1600, distance, 50.0);
        System.out.println("Distance: " + distance);
    }

}