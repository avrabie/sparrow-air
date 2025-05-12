package com.execodex.sparrowair2.services;

import com.execodex.sparrowair2.entities.AirlineFleet;
import com.execodex.sparrowair2.repositories.AirlineFleetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AirlineFleetServiceTest {

    @Mock
    private AirlineFleetRepository airlineFleetRepository;

    @InjectMocks
    private AirlineFleetService airlineFleetService;

    private AirlineFleet testAirlineFleet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testAirlineFleet = AirlineFleet.builder()
                .id(1L)
                .aircraftTypeIcao("B738")
                .airlineIcao("AAL")
                .aircraftAge(LocalDate.of(2015, 5, 12))
                .seatConfiguration("3-3")
                .hasWifi(true)
                .hasPowerOutlets(true)
                .hasEntertainmentSystem(true)
                .firstClassSeats(8)
                .businessSeats(24)
                .economySeats(126)
                .build();
    }

    @Test
    void getAllAirlineFleet() {
        when(airlineFleetRepository.findAll()).thenReturn(Flux.just(testAirlineFleet));

        StepVerifier.create(airlineFleetService.getAllAirlineFleet())
                .expectNext(testAirlineFleet)
                .verifyComplete();
    }

    @Test
    void getAirlineFleetById() {
        when(airlineFleetRepository.findById(1L)).thenReturn(Mono.just(testAirlineFleet));

        StepVerifier.create(airlineFleetService.getAirlineFleetById(1L))
                .expectNext(testAirlineFleet)
                .verifyComplete();
    }

    @Test
    void getAirlineFleetByAirlineIcao() {
        when(airlineFleetRepository.findByAirlineIcao("AAL")).thenReturn(Flux.just(testAirlineFleet));

        StepVerifier.create(airlineFleetService.getAirlineFleetByAirlineIcao("AAL"))
                .expectNext(testAirlineFleet)
                .verifyComplete();
    }

    @Test
    void getAirlineFleetByAircraftTypeIcao() {
        when(airlineFleetRepository.findByAircraftTypeIcao("B738")).thenReturn(Flux.just(testAirlineFleet));

        StepVerifier.create(airlineFleetService.getAirlineFleetByAircraftTypeIcao("B738"))
                .expectNext(testAirlineFleet)
                .verifyComplete();
    }

    @Test
    void createAirlineFleet() {
        when(airlineFleetRepository.insert(any(AirlineFleet.class))).thenReturn(Mono.just(testAirlineFleet));

        StepVerifier.create(airlineFleetService.createAirlineFleet(testAirlineFleet))
                .expectNext(testAirlineFleet)
                .verifyComplete();
    }

    @Test
    void updateAirlineFleet() {
        when(airlineFleetRepository.save(any(AirlineFleet.class))).thenReturn(Mono.just(testAirlineFleet));

        StepVerifier.create(airlineFleetService.updateAirlineFleet(1L, testAirlineFleet))
                .expectNext(testAirlineFleet)
                .verifyComplete();
    }

    @Test
    void deleteAirlineFleet() {
        when(airlineFleetRepository.findById(1L)).thenReturn(Mono.just(testAirlineFleet));
        when(airlineFleetRepository.delete(testAirlineFleet)).thenReturn(Mono.empty());

        StepVerifier.create(airlineFleetService.deleteAirlineFleet(1L))
                .verifyComplete();
    }



    @Test
    void getTotalAircraftCountByAirlineIcao() {
        when(airlineFleetRepository.countByAirlineIcao("AAL")).thenReturn(Mono.just(10L));

        StepVerifier.create(airlineFleetService.getTotalAircraftCountByAirlineIcao("AAL"))
                .expectNext(10L)
                .verifyComplete();
    }
}
