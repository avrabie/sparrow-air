package com.execodex.sparrowair2.services;

import com.execodex.sparrowair2.entities.Airport2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Airport2ServiceTest {

    @Test
    void testDistanceBetweenTwoAirports_ShortDistance() {
        Airport2 airport21 = Airport2.builder()
                .icaoCode("JFK")
                .latitude(40.6413)
                .longitude(-73.7781)
                .build();

        Airport2 airport2 = Airport2.builder()
                .icaoCode("LGA")
                .latitude(40.7769)
                .longitude(-73.8740)
                .build();

        AirportService airportService = new AirportService(null);

        double distance = airportService.distance(airport21, airport2);

        assertTrue(distance > 0);
        assertEquals(17, distance, 1.0);
    }

    @Test
    void testDistanceBetweenTwoAirports_LongDistance() {
        Airport2 airport21 = Airport2.builder()
                .icaoCode("LAX")
                .latitude(33.9416)
                .longitude(-118.4085)
                .build();

        Airport2 airport2 = Airport2.builder()
                .icaoCode("HND")
                .latitude(35.5494)
                .longitude(139.7798)
                .build();

        AirportService airportService = new AirportService(null);

        double distance = airportService.distance(airport21, airport2);

        assertTrue(distance > 0);
        assertEquals(8799.8, distance, 50.0);
    }

    @Test
    void testDistanceBetweenSameAirport() {
        Airport2 airport2 = Airport2.builder()
                .icaoCode("ORD")
                .latitude(41.9742)
                .longitude(-87.9073)
                .build();

        AirportService airportService = new AirportService(null);

        double distance = airportService.distance(airport2, airport2);

        assertEquals(0.0, distance, 0.01);
    }

    // chisinau 46.9352° N, 28.9349° E
    // basel 47.5596° N, 7.5886° E
    @Test
    void testDistanceBetweenTwoAirports_ZeroDistance() {
        Airport2 airport21 = Airport2.builder()
                .icaoCode("KIV")
                .latitude(46.9352)
                .longitude(28.9349)
                .build();

        Airport2 airport2 = Airport2.builder()
                .icaoCode("BSL")
                .latitude(47.5596)
                .longitude(7.5886)
                .build();


        AirportService airportService = new AirportService(null);
        double distance = airportService.distance(airport21, airport2);
        assertTrue(distance > 0);
        assertEquals(1600, distance, 50.0);
        System.out.println("Distance: " + distance);
    }

}