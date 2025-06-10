package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.configs.AbstractTestcontainersTest;
import com.execodex.sparrowair2.entities.AirportNew;
import com.execodex.sparrowair2.repositories.AirportNewRepository;
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
public class AirportNew2Test extends AbstractTestcontainersTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private AirportNewRepository airportRepository;

    @BeforeEach
    public void setUp() {
        // Clear the repository before each test
        airportRepository.deleteAll().block();
    }

    @Test
    public void testCreateAirport() {


        AirportNew airport = AirportNew.builder()
                .icaoCode("KLA3")
                .name("Los Angeles International Airport")
                .city("Los Angeles")
                .country("USA")
                .icaoRegion("North America")
                .latitude(33.9425)
                .longitude(-118.408)
                .build();

        webTestClient.post()
                .uri("/airportsnew")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(airport), AirportNew.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AirportNew.class)
                .isEqualTo(airport);
    }

    @Test
    public void testGetAllAirports() {
        // Create a test airport
        AirportNew airport = AirportNew.builder()
                .icaoCode("KJFK")
                .name("John F. Kennedy International Airport")
                .city("New York")
                .country("USA")
                .icaoRegion("North America")
                .latitude(40.6413)
                .longitude(-73.7781)
                .build();

        // Save the airport and wait for it to complete
        AirportNew savedAirport = airportRepository.insert(airport).block();

        webTestClient.get()
                .uri("/airportsnew")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AirportNew.class)
                .contains(airport);
    }

    @Test
    public void testGetAirportByIcaoCode() {
        // Create a test airport
        AirportNew airport = AirportNew.builder()
                .icaoCode("KORD")
                .name("O'Hare International Airport")
                .city("Chicago")
                .country("USA")
                .icaoRegion("North America")
                .latitude(41.9742)
                .longitude(-87.9073)
                .build();

        // Save the airport and wait for it to complete
        AirportNew savedAirport = airportRepository.insert(airport).block();

        webTestClient.get()
                .uri("/airportsnew/{icaoCode}", "KORD")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AirportNew.class)
                .isEqualTo(airport);
    }

    @Test
    public void testUpdateAirport() {
        // Create a test airport
        AirportNew airport = AirportNew.builder()
                .icaoCode("KSFO")
                .name("San Francisco International Airport")
                .city("San Francisco")
                .country("USA")
                .icaoRegion("North America")
                .latitude(37.6213)
                .longitude(-122.3790)
                .build();

        // Save the airport and wait for it to complete
        AirportNew savedAirport = airportRepository.insert(airport).block();

        // Update the airport
        AirportNew updatedAirport = AirportNew.builder()
                .icaoCode("KSFO") // Same ICAO code
                .name("San Francisco International Airport")
                .city("San Francisco")
                .country("USA")
                .icaoRegion("North America")
                .latitude(37.6213)
                .longitude(-122.3791) // Updated longitude
                .build();

        webTestClient.put()
                .uri("/airportsnew/{icaoCode}", "KSFO")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedAirport), AirportNew.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AirportNew.class)
                .isEqualTo(updatedAirport);
    }

    @Test
    public void testDeleteAirport() {
        // Create a test airport
        AirportNew airport = AirportNew.builder()
                .icaoCode("KATL")
                .name("Hartsfield-Jackson Atlanta International Airport")
                .city("Atlanta")
                .country("USA")
                .icaoRegion("North America")
                .latitude(33.6407)
                .longitude(-84.4277)
                .build();

        // Save the airport and wait for it to complete
        AirportNew savedAirport = airportRepository.insert(airport).block();

        webTestClient.delete()
                .uri("/airportsnew/{icaoCode}", "KATL")
                .exchange()
                .expectStatus().isNoContent();

        // Verify the airport was deleted
        webTestClient.get()
                .uri("/airportsnew/{icaoCode}", "KATL")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }
}
