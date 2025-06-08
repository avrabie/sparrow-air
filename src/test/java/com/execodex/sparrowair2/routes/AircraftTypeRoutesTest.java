package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.configs.AbstractTestcontainersTest;
import com.execodex.sparrowair2.entities.AircraftType;
import com.execodex.sparrowair2.repositories.AircraftTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AircraftTypeRoutesTest extends AbstractTestcontainersTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private AircraftTypeRepository aircraftTypeRepository;

    @BeforeEach
    public void setUp() {
        // Clear the repository before each test
        aircraftTypeRepository.deleteAll().block();
    }

    @Test
    public void testCreateAircraftType() {
        AircraftType aircraftType = AircraftType.builder()
                .icaoCode("B738")
                .modelName("Boeing 737-800")
                .manufacturer("Boeing")
                .maxRangeKm(5765)
                .mtow(79010)
                .build();

        webTestClient.post()
                .uri("/aircraft-types")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(aircraftType), AircraftType.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AircraftType.class)
                .isEqualTo(aircraftType);
    }

    @Test
    public void testGetAllAircraftTypes() {
        // Create a test aircraft type
        AircraftType aircraftType = AircraftType.builder()
                .icaoCode("A320")
                .modelName("Airbus A320")
                .manufacturer("Airbus")
                .maxRangeKm(6150)
                .mtow(77000)
                .build();

        // Save the aircraft type and wait for it to complete
        AircraftType savedAircraftType = aircraftTypeRepository.insert(aircraftType).block();

        webTestClient.get()
                .uri("/aircraft-types")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AircraftType.class)
                .contains(aircraftType);
    }

    @Test
    public void testGetAllAircraftTypesXml() {
        // Create a test aircraft type
        AircraftType aircraftType = AircraftType.builder()
                .icaoCode("A321")
                .modelName("Airbus A321")
                .manufacturer("Airbus")
                .maxRangeKm(5950)
                .mtow(89000)
                .build();

        // Save the aircraft type and wait for it to complete
        AircraftType savedAircraftType = aircraftTypeRepository.insert(aircraftType).block();

        webTestClient.get()
                .uri("/aircraft-types")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AircraftType.class)
                .contains(aircraftType);
    }

    @Test
    public void testGetAircraftTypeByIcaoCode() {
        // Create a test aircraft type
        AircraftType aircraftType = AircraftType.builder()
                .icaoCode("B777")
                .modelName("Boeing 777")
                .manufacturer("Boeing")
                .maxRangeKm(9700)
                .mtow(299370)
                .build();

        // Save the aircraft type and wait for it to complete
        AircraftType savedAircraftType = aircraftTypeRepository.insert(aircraftType).block();

        webTestClient.get()
                .uri("/aircraft-types/{icaoCode}", "B777")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AircraftType.class)
                .isEqualTo(aircraftType);
    }

    @Test
    public void testUpdateAircraftType() {
        // Create a test aircraft type
        AircraftType aircraftType = AircraftType.builder()
                .icaoCode("A380")
                .modelName("Airbus A380")
                .manufacturer("Airbus")
                .maxRangeKm(14800)
                .mtow(575000)
                .build();

        // Save the aircraft type and wait for it to complete
        AircraftType savedAircraftType = aircraftTypeRepository.insert(aircraftType).block();

        // Update the aircraft type
        AircraftType updatedAircraftType = AircraftType.builder()
                .icaoCode("A380") // Same ICAO code
                .modelName("Airbus A380-800") // Updated model name
                .manufacturer("Airbus")
                .maxRangeKm(14800)
                .mtow(575000)
                .build();

        webTestClient.put()
                .uri("/aircraft-types/{icaoCode}", "A380")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedAircraftType), AircraftType.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AircraftType.class)
                .isEqualTo(updatedAircraftType);
    }

    @Test
    public void testDeleteAircraftType() {
        // Create a test aircraft type
        AircraftType aircraftType = AircraftType.builder()
                .icaoCode("B747")
                .modelName("Boeing 747")
                .manufacturer("Boeing")
                .maxRangeKm(14200)
                .mtow(447700)
                .build();

        // Save the aircraft type and wait for it to complete
        AircraftType savedAircraftType = aircraftTypeRepository.insert(aircraftType).block();

        webTestClient.delete()
                .uri("/aircraft-types/{icaoCode}", "B747")
                .exchange()
                .expectStatus().isNoContent();

        // Verify the aircraft type was deleted
        webTestClient.get()
                .uri("/aircraft-types/{icaoCode}", "B747")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }
}
