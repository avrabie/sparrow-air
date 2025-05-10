package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.configs.AbstractTestcontainersTest;
import com.execodex.sparrowair2.entities.Airport;
import com.execodex.sparrowair2.repositories.AirportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
//        "spring.r2dbc.pool.initial-size=1",
//        "spring.r2dbc.pool.max-size=1",
//        "local.server.port=0"
})
public class AirportTest extends AbstractTestcontainersTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private AirportRepository airportRepository;

    @BeforeEach
    public void setUp() {
        // Clear the repository before each test
        airportRepository.deleteAll().block();
    }

    @Test
    public void testCreateAirport() {


        Airport airport = Airport.builder()
                .icaoCode("KLA3")
                .name("Los Angeles International Airport")
                .city("Los Angeles")
                .country("USA")
                .timezone("America/Los_Angeles")
                .latitude(33.9425)
                .longitude(-118.408)
                .build();

        webTestClient.post()
                .uri("/airports")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(airport), Airport.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Airport.class)
                .isEqualTo(airport);
    }

    @Test
    public void testGetAllAirports() {
        // Create a test airport
        Airport airport = Airport.builder()
                .icaoCode("KJFK")
                .name("John F. Kennedy International Airport")
                .city("New York")
                .country("USA")
                .timezone("America/New_York")
                .latitude(40.6413)
                .longitude(-73.7781)
                .build();

        // Save the airport and wait for it to complete
        Airport savedAirport = airportRepository.insert(airport).block();

        webTestClient.get()
                .uri("/airports")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Airport.class)
                .contains(airport);
    }

    @Test
    public void testGetAirportByIcaoCode() {
        // Create a test airport
        Airport airport = Airport.builder()
                .icaoCode("KORD")
                .name("O'Hare International Airport")
                .city("Chicago")
                .country("USA")
                .timezone("America/Chicago")
                .latitude(41.9742)
                .longitude(-87.9073)
                .build();

        // Save the airport and wait for it to complete
        Airport savedAirport = airportRepository.insert(airport).block();

        webTestClient.get()
                .uri("/airports/{icaoCode}", "KORD")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Airport.class)
                .isEqualTo(airport);
    }

    @Test
    public void testUpdateAirport() {
        // Create a test airport
        Airport airport = Airport.builder()
                .icaoCode("KSFO")
                .name("San Francisco International Airport")
                .city("San Francisco")
                .country("USA")
                .timezone("America/Los_Angeles")
                .latitude(37.6213)
                .longitude(-122.3790)
                .build();

        // Save the airport and wait for it to complete
        Airport savedAirport = airportRepository.insert(airport).block();

        // Update the airport
        Airport updatedAirport = Airport.builder()
                .icaoCode("KSFO") // Same ICAO code
                .name("San Francisco International Airport")
                .city("San Francisco")
                .country("USA")
                .timezone("America/Los_Angeles")
                .latitude(37.6213)
                .longitude(-122.3791) // Updated longitude
                .build();

        webTestClient.put()
                .uri("/airports/{icaoCode}", "KSFO")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedAirport), Airport.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Airport.class)
                .isEqualTo(updatedAirport);
    }

    @Test
    public void testDeleteAirport() {
        // Create a test airport
        Airport airport = Airport.builder()
                .icaoCode("KATL")
                .name("Hartsfield-Jackson Atlanta International Airport")
                .city("Atlanta")
                .country("USA")
                .timezone("America/New_York")
                .latitude(33.6407)
                .longitude(-84.4277)
                .build();

        // Save the airport and wait for it to complete
        Airport savedAirport = airportRepository.insert(airport).block();

        webTestClient.delete()
                .uri("/airports/{icaoCode}", "KATL")
                .exchange()
                .expectStatus().isNoContent();

        // Verify the airport was deleted
        webTestClient.get()
                .uri("/airports/{icaoCode}", "KATL")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }
}
