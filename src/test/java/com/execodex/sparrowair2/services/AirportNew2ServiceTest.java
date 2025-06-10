package com.execodex.sparrowair2.services;

import com.execodex.sparrowair2.entities.skybrary.AirportNew;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AirportNew2ServiceTest {

    @Test
    void testDistanceBetweenTwoAirports_ShortDistance() {
        AirportNew airport21 = AirportNew.builder()
                .icaoCode("JFK")
                .latitude(40.6413)
                .longitude(-73.7781)
                .build();

        AirportNew airport = AirportNew.builder()
                .icaoCode("LGA")
                .latitude(40.7769)
                .longitude(-73.8740)
                .build();

        AirportNewService airportService = new AirportNewService(null);

        double distance = airportService.distance(airport21, airport);

        assertTrue(distance > 0);
        assertEquals(17, distance, 1.0);
    }

    @Test
    void testDistanceBetweenTwoAirports_LongDistance() {
        AirportNew airport21 = AirportNew.builder()
                .icaoCode("LAX")
                .latitude(33.9416)
                .longitude(-118.4085)
                .build();

        AirportNew airport = AirportNew.builder()
                .icaoCode("HND")
                .latitude(35.5494)
                .longitude(139.7798)
                .build();

        AirportNewService airportService = new AirportNewService(null);

        double distance = airportService.distance(airport21, airport);

        assertTrue(distance > 0);
        assertEquals(8799.8, distance, 50.0);
    }

    @Test
    void testDistanceBetweenSameAirport() {
        AirportNew airport = AirportNew.builder()
                .icaoCode("ORD")
                .latitude(41.9742)
                .longitude(-87.9073)
                .build();

        AirportNewService airportService = new AirportNewService(null);

        double distance = airportService.distance(airport, airport);

        assertEquals(0.0, distance, 0.01);
    }

    // chisinau 46.9352° N, 28.9349° E
    // basel 47.5596° N, 7.5886° E
    @Test
    void testDistanceBetweenTwoAirports_ZeroDistance() {
        AirportNew airport21 = AirportNew.builder()
                .icaoCode("KIV")
                .latitude(46.9352)
                .longitude(28.9349)
                .build();

        AirportNew airport = AirportNew.builder()
                .icaoCode("BSL")
                .latitude(47.5596)
                .longitude(7.5886)
                .build();


        AirportNewService airportService = new AirportNewService(null);
        double distance = airportService.distance(airport21, airport);
        assertTrue(distance > 0);
        assertEquals(1600, distance, 50.0);
        System.out.println("Distance: " + distance);
    }

}