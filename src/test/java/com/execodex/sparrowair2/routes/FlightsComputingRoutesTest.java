package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.configs.AbstractTestcontainersTest;
import com.execodex.sparrowair2.entities.*;
import com.execodex.sparrowair2.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Collection;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FlightsComputingRoutesTest extends AbstractTestcontainersTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private AirportNewRepository airportNewRepository;

    @Autowired
    private AirlineRepository airlineRepository;

    @Autowired
    private AircraftRepository aircraftRepository;

    @Autowired
    private AirlineFleetRepository airlineFleetRepository;

    @BeforeEach
    public void setUp() {
        // Clear existing flights
        flightRepository.deleteAll().block();

        // Create test airports if they don't exist
        AirportNew lhr = AirportNew.builder()
                .icaoCode("EGLL")
                .name("London Heathrow Airport")
                .city("London")
                .country("United Kingdom")
                .latitude(51.4775)
                .longitude(-0.4614)
                .build();

        AirportNew cdg = AirportNew.builder()
                .icaoCode("LFPG")
                .name("Paris Charles de Gaulle Airport")
                .city("Paris")
                .country("France")
                .latitude(49.0097)
                .longitude(2.5479)
                .build();

        AirportNew jfk = AirportNew.builder()
                .icaoCode("KJFK")
                .name("John F. Kennedy International Airport")
                .city("New York")
                .country("United States")
                .latitude(40.6413)
                .longitude(-73.7781)
                .build();

        // Save airports if they don't exist
        airportNewRepository.findById("EGLL")
                .switchIfEmpty(Mono.defer(() -> airportNewRepository.insert(lhr)))
                .block();
        airportNewRepository.findById("LFPG")
                .switchIfEmpty(Mono.defer(() -> airportNewRepository.insert(cdg)))
                .block();
        airportNewRepository.findById("KJFK")
                .switchIfEmpty(Mono.defer(() -> airportNewRepository.insert(jfk)))
                .block();

        // Create test airlines
        Airline britishAirways = Airline.builder()
                .icaoCode("BAW")
                .name("British Airways")
                .headquarters("London, UK")
                .contactNumber("+44 20 1234 5678")
                .website("https://www.britishairways.com")
                .build();

        Airline airFrance = Airline.builder()
                .icaoCode("AFR")
                .name("Air France")
                .headquarters("Paris, France")
                .contactNumber("+33 1 2345 6789")
                .website("https://www.airfrance.com")
                .build();

        // Save airlines if they don't exist
        airlineRepository.findById("BAW")
                .switchIfEmpty(Mono.defer(() -> airlineRepository.insert(britishAirways)))
                .block();
        airlineRepository.findById("AFR")
                .switchIfEmpty(Mono.defer(() -> airlineRepository.insert(airFrance)))
                .block();

        // Create test aircraft types
        Aircraft boeing777 = Aircraft.builder()
                .icaoCode("B777")
                .name("Boeing 777")
                .manufacturer("Boeing")
                .rangeNm(15843)
                .maxTakeOffWeightKg(351534)
                .build();

        Aircraft airbusA320 = Aircraft.builder()
                .icaoCode("A320")
                .name("Airbus A320")
                .manufacturer("Airbus")
                .rangeNm(3300)
                .maxTakeOffWeightKg(78000)
                .build();



        // Save aircraft types if they don't exist
        aircraftRepository.findById("B777")
                .switchIfEmpty(Mono.defer(() -> aircraftRepository.insert(boeing777)))
                .block();
        aircraftRepository.findById("A320")
                .switchIfEmpty(Mono.defer(() -> aircraftRepository.insert(airbusA320)))
                .block();

        // Create test airline fleet entries
        AirlineFleet bawB777 = AirlineFleet.builder()
                .aircraftTypeIcao("B777")
                .airlineIcao("BAW")
                .registrationNumber("G-ZZZA")
                .aircraftAge(LocalDate.of(2015, 1, 1))
                .seatConfiguration("3-4-3")
                .hasWifi(true)
                .hasPowerOutlets(true)
                .hasEntertainmentSystem(true)
                .firstClassSeats(14)
                .businessSeats(48)
                .premiumEconomySeats(40)
                .economySeats(294)
                .build();

        AirlineFleet afrA320 = AirlineFleet.builder()
                .aircraftTypeIcao("A320")
                .airlineIcao("AFR")
                .registrationNumber("F-HBNA")
                .aircraftAge(LocalDate.of(2018, 5, 15))
                .seatConfiguration("3-3")
                .hasWifi(true)
                .hasPowerOutlets(true)
                .hasEntertainmentSystem(true)
                .firstClassSeats(0)
                .businessSeats(12)
                .premiumEconomySeats(0)
                .economySeats(168)
                .build();

        // Save airline fleet entries if they don't exist
        AirlineFleet savedBawB777 = airlineFleetRepository.findByRegistrationNumber("G-ZZZA")
                .switchIfEmpty(Mono.defer(() -> airlineFleetRepository.insert(bawB777)))
                .block();
        AirlineFleet savedAfrA320 = airlineFleetRepository.findByRegistrationNumber("F-HBNA")
                .switchIfEmpty(Mono.defer(() -> airlineFleetRepository.insert(afrA320)))
                .block();

        // Create test flights
        LocalDateTime now = LocalDateTime.now();

        Flight flight1 = Flight.builder()
                .airlineIcaoCode("BAW")
                .flightNumber("BA123")
                .departureAirportIcao("EGLL")
                .arrivalAirportIcao("LFPG")
                .scheduledDeparture(now.plusDays(1))
                .scheduledArrival(now.plusDays(1).plusHours(2))
                .airlineFleetId(savedBawB777.getId())
                .status("Scheduled")
                .build();

        Flight flight2 = Flight.builder()
                .airlineIcaoCode("BAW")
                .flightNumber("BA456")
                .departureAirportIcao("EGLL")
                .arrivalAirportIcao("KJFK")
                .scheduledDeparture(now.plusDays(2))
                .scheduledArrival(now.plusDays(2).plusHours(8))
                .airlineFleetId(savedBawB777.getId())
                .status("Scheduled")
                .build();

        Flight flight3 = Flight.builder()
                .airlineIcaoCode("AFR")
                .flightNumber("AF789")
                .departureAirportIcao("LFPG")
                .arrivalAirportIcao("KJFK")
                .scheduledDeparture(now.plusDays(3))
                .scheduledArrival(now.plusDays(3).plusHours(9))
                .airlineFleetId(savedAfrA320.getId())
                .status("Scheduled")
                .build();

        // Save flights if they don't exist
        flightRepository.findByAirlineIcaoCodeAndFlightNumber("BAW", "BA123")
                .switchIfEmpty(Mono.defer(() -> flightRepository.save(flight1)))
                .block();
        flightRepository.findByAirlineIcaoCodeAndFlightNumber("BAW", "BA456")
                .switchIfEmpty(Mono.defer(() -> flightRepository.save(flight2)))
                .block();
        flightRepository.findByAirlineIcaoCodeAndFlightNumber("AFR", "AF789")
                .switchIfEmpty(Mono.defer(() -> flightRepository.save(flight3)))
                .block();
    }

    @Test
    public void testGetAirportToAirportsFlights() {
        webTestClient.get()
                .uri("/flights-computing/airport-to-airports")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Map.class)
                .consumeWith(response -> {
                    // Verify that the response contains the expected data
                    // The response should be a list of maps, where each map has a departure airport as key
                    // and a collection of arrival airports as value
                    assert response.getResponseBody() != null;
                    assert !response.getResponseBody().isEmpty();

                    // We expect to see EGLL -> [LFPG, KJFK] and LFPG -> [KJFK]
                    boolean foundEgllToLfpg = false;
                    boolean foundEgllToKjfk = false;
                    boolean foundLfpgToKjfk = false;

                    for (Map<String, Collection<String>> map : response.getResponseBody()) {
                        for (Map.Entry<String, Collection<String>> entry : map.entrySet()) {
                            String departureAirport = entry.getKey();
                            Collection<String> arrivalAirports = entry.getValue();

                            if (departureAirport.equals("EGLL")) {
                                for (String arrivalAirport : arrivalAirports) {
                                    if (arrivalAirport.equals("LFPG")) {
                                        foundEgllToLfpg = true;
                                    } else if (arrivalAirport.equals("KJFK")) {
                                        foundEgllToKjfk = true;
                                    }
                                }
                            } else if (departureAirport.equals("LFPG")) {
                                for (String arrivalAirport : arrivalAirports) {
                                    if (arrivalAirport.equals("KJFK")) {
                                        foundLfpgToKjfk = true;
                                    }
                                }
                            }
                        }
                    }

                    assert foundEgllToLfpg : "Expected to find flight from EGLL to LFPG";
                    assert foundEgllToKjfk : "Expected to find flight from EGLL to KJFK";
                    assert foundLfpgToKjfk : "Expected to find flight from LFPG to KJFK";
                });
    }

    @Test
    public void testGetRoute() {
        // Test route from London to Paris
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/flights-computing/route")
                        .queryParam("departure", "EGLL")
                        .queryParam("arrival", "LFPG")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Flight.class)
                .consumeWith(response -> {
                    assert response.getResponseBody() != null;
                    assert !response.getResponseBody().isEmpty();

                    // Verify that the route contains a flight from EGLL to LFPG
                    boolean foundDirectFlight = false;
                    for (Flight flight : response.getResponseBody()) {
                        if (flight.getDepartureAirportIcao().equals("EGLL") && 
                            flight.getArrivalAirportIcao().equals("LFPG")) {
                            foundDirectFlight = true;
                            break;
                        }
                    }

                    assert foundDirectFlight : "Expected to find a direct flight from EGLL to LFPG";
                });

        // Test route from London to New York
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/flights-computing/route")
                        .queryParam("departure", "EGLL")
                        .queryParam("arrival", "KJFK")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Flight.class)
                .consumeWith(response -> {
                    assert response.getResponseBody() != null;
                    assert !response.getResponseBody().isEmpty();

                    // Verify that the route starts at EGLL and ends at KJFK
                    // This could be a direct flight or a route with connections
                    List<Flight> flights = response.getResponseBody();
                    assert !flights.isEmpty() : "Expected a non-empty route";

                    // Check that the first flight departs from EGLL
                    Flight firstFlight = flights.get(0);
                    assert firstFlight.getDepartureAirportIcao().equals("EGLL") : 
                        "Expected the route to start from EGLL";

                    // Check that the last flight arrives at KJFK
                    Flight lastFlight = flights.get(flights.size() - 1);
                    assert lastFlight.getArrivalAirportIcao().equals("KJFK") : 
                        "Expected the route to end at KJFK";
                });
    }

    @Test
    public void testGetRouteMinimumCost() {
        // Test minimum cost route from London to Paris
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/flights-computing/route-minimum-cost")
                        .queryParam("departure", "EGLL")
                        .queryParam("arrival", "LFPG")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Flight.class)
                .consumeWith(response -> {
                    assert response.getResponseBody() != null;
                    assert !response.getResponseBody().isEmpty();

                    // Verify that the route contains a flight from EGLL to LFPG
                    boolean foundDirectFlight = false;
                    for (Flight flight : response.getResponseBody()) {
                        if (flight.getDepartureAirportIcao().equals("EGLL") && 
                            flight.getArrivalAirportIcao().equals("LFPG")) {
                            foundDirectFlight = true;
                            break;
                        }
                    }

                    assert foundDirectFlight : "Expected to find a direct flight from EGLL to LFPG";
                });

        // Test minimum cost route from London to New York
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/flights-computing/route-minimum-cost")
                        .queryParam("departure", "EGLL")
                        .queryParam("arrival", "KJFK")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Flight.class)
                .consumeWith(response -> {
                    assert response.getResponseBody() != null;
                    assert !response.getResponseBody().isEmpty();

                    // Verify that the route starts at EGLL and ends at KJFK
                    // This could be a direct flight or a route with connections
                    List<Flight> flights = response.getResponseBody();
                    assert !flights.isEmpty() : "Expected a non-empty route";

                    // Check that the first flight departs from EGLL
                    Flight firstFlight = flights.get(0);
                    assert firstFlight.getDepartureAirportIcao().equals("EGLL") : 
                        "Expected the route to start from EGLL";

                    // Check that the last flight arrives at KJFK
                    Flight lastFlight = flights.get(flights.size() - 1);
                    assert lastFlight.getArrivalAirportIcao().equals("KJFK") : 
                        "Expected the route to end at KJFK";
                });
    }
}
