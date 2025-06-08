package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.configs.AbstractTestcontainersTest;
import com.execodex.sparrowair2.entities.Airport2;
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
public class Airport2Test extends AbstractTestcontainersTest {

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


        Airport2 airport2 = Airport2.builder()
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
                .body(Mono.just(airport2), Airport2.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Airport2.class)
                .isEqualTo(airport2);
    }

    @Test
    public void testGetAllAirports() {
        // Create a test airport
        Airport2 airport2 = Airport2.builder()
                .icaoCode("KJFK")
                .name("John F. Kennedy International Airport")
                .city("New York")
                .country("USA")
                .timezone("America/New_York")
                .latitude(40.6413)
                .longitude(-73.7781)
                .build();

        // Save the airport and wait for it to complete
        Airport2 savedAirport2 = airportRepository.insert(airport2).block();

        webTestClient.get()
                .uri("/airports")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Airport2.class)
                .contains(airport2);
    }

    @Test
    public void testGetAirportByIcaoCode() {
        // Create a test airport
        Airport2 airport2 = Airport2.builder()
                .icaoCode("KORD")
                .name("O'Hare International Airport")
                .city("Chicago")
                .country("USA")
                .timezone("America/Chicago")
                .latitude(41.9742)
                .longitude(-87.9073)
                .build();

        // Save the airport and wait for it to complete
        Airport2 savedAirport2 = airportRepository.insert(airport2).block();

        webTestClient.get()
                .uri("/airports/{icaoCode}", "KORD")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Airport2.class)
                .isEqualTo(airport2);
    }

    @Test
    public void testUpdateAirport() {
        // Create a test airport
        Airport2 airport2 = Airport2.builder()
                .icaoCode("KSFO")
                .name("San Francisco International Airport")
                .city("San Francisco")
                .country("USA")
                .timezone("America/Los_Angeles")
                .latitude(37.6213)
                .longitude(-122.3790)
                .build();

        // Save the airport and wait for it to complete
        Airport2 savedAirport2 = airportRepository.insert(airport2).block();

        // Update the airport
        Airport2 updatedAirport2 = Airport2.builder()
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
                .body(Mono.just(updatedAirport2), Airport2.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Airport2.class)
                .isEqualTo(updatedAirport2);
    }

    @Test
    public void testDeleteAirport() {
        // Create a test airport
        Airport2 airport2 = Airport2.builder()
                .icaoCode("KATL")
                .name("Hartsfield-Jackson Atlanta International Airport")
                .city("Atlanta")
                .country("USA")
                .timezone("America/New_York")
                .latitude(33.6407)
                .longitude(-84.4277)
                .build();

        // Save the airport and wait for it to complete
        Airport2 savedAirport2 = airportRepository.insert(airport2).block();

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
