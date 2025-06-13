package com.execodex.sparrowair2.services;

import com.execodex.sparrowair2.entities.caa.FaaAircraftRegistration;
import com.execodex.sparrowair2.repositories.FaaAircraftRegistrationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DuplicateKeyException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit tests for FaaAircraftRegistrationService.
 */
class FaaAircraftRegistrationServiceTest {

    @Mock
    private FaaAircraftRegistrationRepository faaAircraftRegistrationRepository;

    @InjectMocks
    private FaaAircraftRegistrationService faaAircraftRegistrationService;

    private FaaAircraftRegistration testRegistration;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create a test registration using the data from the issue description
        testRegistration = FaaAircraftRegistration.builder()
                .nNumber("10020")
                .serialNumber("20044C")
                .aircraftMfrModelCode("5460186")
                .engineMfrModelCode("41514")
                .yearManufactured("1995")
                .typeRegistrant("4")
                .registrantName("STEPHENS DAVID SR")
                .street1("164 DIEHL RANCH RD")
                .street2(null)
                .registrantCity("PLAINS")
                .registrantState("MT")
                .registrantZipCode("598599291")
                .registrantRegion("S")
                .countyMail("089")
                .countryMail("US")
                .lastActivityDate("20250207")
                .certificateIssueDate("20250207")
                .airworthinessClassificationCode("1N")
                .approvedOperationCodes("4")
                .typeAircraft("1")
                .typeEngine("V")
                .statusCode("50")
                .modeSCode(null)
                .fractionalOwnership("19")
                .airworthinessDate("STEPHENS DENNA")
                .otherName1(null)
                .otherName2(null)
                .otherName3(null)
                .otherName4(null)
                .otherName5("20320229")
                .expirationDate("00695562")
                .uniqueId(null)
                .kitMfr(null)
                .kitModel("A0076B")
                .modeScodeHex(null)
                .build();
    }

    @Test
    void testCreateFaaAircraftRegistration_Success() {
        // Mock the repository to return the test registration when insert is called
        when(faaAircraftRegistrationRepository.insert(any(FaaAircraftRegistration.class)))
                .thenReturn(Mono.just(testRegistration));

        // Call the service method
        Mono<FaaAircraftRegistration> result = faaAircraftRegistrationService.createFaaAircraftRegistration(testRegistration);

        // Verify the result
        StepVerifier.create(result)
                .expectNext(testRegistration)
                .verifyComplete();
    }

    @Test
    void testCreateFaaAircraftRegistration_DuplicateKey() {
        // Mock the repository to throw a DuplicateKeyException when insert is called
        when(faaAircraftRegistrationRepository.insert(any(FaaAircraftRegistration.class)))
                .thenReturn(Mono.error(new DuplicateKeyException("Duplicate key")));

        // Call the service method
        Mono<FaaAircraftRegistration> result = faaAircraftRegistrationService.createFaaAircraftRegistration(testRegistration);

        // Verify the result
        StepVerifier.create(result)
                .expectError(DuplicateKeyException.class)
                .verify();
    }

    @Test
    void testGetFaaAircraftRegistrationByNNumber_Success() {
        // Mock the repository to return the test registration when findById is called
        when(faaAircraftRegistrationRepository.findById("10020"))
                .thenReturn(Mono.just(testRegistration));

        // Call the service method
        Mono<FaaAircraftRegistration> result = faaAircraftRegistrationService.getFaaAircraftRegistrationByNNumber("10020");

        // Verify the result
        StepVerifier.create(result)
                .expectNext(testRegistration)
                .verifyComplete();
    }

    @Test
    void testGetFaaAircraftRegistrationByNNumber_NotFound() {
        // Mock the repository to return an empty Mono when findById is called
        when(faaAircraftRegistrationRepository.findById("10020"))
                .thenReturn(Mono.empty());

        // Call the service method
        Mono<FaaAircraftRegistration> result = faaAircraftRegistrationService.getFaaAircraftRegistrationByNNumber("10020");

        // Verify the result
        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void testGetAllFaaAircraftRegistrations() {
        // Mock the repository to return a Flux with the test registration when findAll is called
        when(faaAircraftRegistrationRepository.findAll())
                .thenReturn(Flux.just(testRegistration));

        // Call the service method
        Flux<FaaAircraftRegistration> result = faaAircraftRegistrationService.getAllFaaAircraftRegistrations();

        // Verify the result
        StepVerifier.create(result)
                .expectNext(testRegistration)
                .verifyComplete();
    }

    @Test
    void testUpdateFaaAircraftRegistration_Success() {
        // Mock the repository to return the test registration when save is called
        when(faaAircraftRegistrationRepository.save(any(FaaAircraftRegistration.class)))
                .thenReturn(Mono.just(testRegistration));

        // Call the service method
        Mono<FaaAircraftRegistration> result = faaAircraftRegistrationService.updateFaaAircraftRegistration("10020", testRegistration);

        // Verify the result
        StepVerifier.create(result)
                .expectNext(testRegistration)
                .verifyComplete();
    }

    @Test
    void testDeleteFaaAircraftRegistration_Success() {
        // Mock the repository to return the test registration when findById is called
        when(faaAircraftRegistrationRepository.findById("10020"))
                .thenReturn(Mono.just(testRegistration));

        // Mock the repository to return an empty Mono when delete is called
        when(faaAircraftRegistrationRepository.delete(any(FaaAircraftRegistration.class)))
                .thenReturn(Mono.empty());

        // Call the service method
        Mono<Void> result = faaAircraftRegistrationService.deleteFaaAircraftRegistration("10020");

        // Verify the result
        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void testDeleteFaaAircraftRegistration_NotFound() {
        // Mock the repository to return an empty Mono when findById is called
        when(faaAircraftRegistrationRepository.findById("10020"))
                .thenReturn(Mono.empty());

        // Call the service method
        Mono<Void> result = faaAircraftRegistrationService.deleteFaaAircraftRegistration("10020");

        // Verify the result
        StepVerifier.create(result)
                .verifyComplete();
    }
}