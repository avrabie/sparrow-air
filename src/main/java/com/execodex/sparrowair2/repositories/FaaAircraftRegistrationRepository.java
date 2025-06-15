package com.execodex.sparrowair2.repositories;

import com.execodex.sparrowair2.entities.caa.FaaAircraftRegistration;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface FaaAircraftRegistrationRepository extends ReactiveCrudRepository<FaaAircraftRegistration, String> {
    // The primary key of FaaAircraftRegistration is nNumber, which is a String
    // ReactiveCrudRepository provides basic CRUD operations with reactive return types

    @Query("INSERT INTO faa_aircraft_registration (n_number, serial_number, aircraft_mfr_model_code, engine_mfr_model_code, " +
            "year_manufactured, type_registrant, registrant_name, street1, street2, registrant_city, registrant_state, " +
            "registrant_zip_code, registrant_region, county_mail, country_mail, last_activity_date, certificate_issue_date, " +
            "airworthiness_classification_code, approved_operation_codes, type_aircraft, type_engine, status_code, " +
            "mode_s_code, fractional_ownership, airworthiness_date, other_name1, other_name2, other_name3, other_name4, " +
            "other_name5, expiration_date, unique_id, kit_mfr, kit_model, mode_scode_hex) " +
            "VALUES (:#{#faaAircraftRegistration.nNumber}, :#{#faaAircraftRegistration.serialNumber}, " +
            ":#{#faaAircraftRegistration.aircraftMfrModelCode}, :#{#faaAircraftRegistration.engineMfrModelCode}, " +
            ":#{#faaAircraftRegistration.yearManufactured}, :#{#faaAircraftRegistration.typeRegistrant}, " +
            ":#{#faaAircraftRegistration.registrantName}, :#{#faaAircraftRegistration.street1}, " +
            ":#{#faaAircraftRegistration.street2}, :#{#faaAircraftRegistration.registrantCity}, " +
            ":#{#faaAircraftRegistration.registrantState}, :#{#faaAircraftRegistration.registrantZipCode}, " +
            ":#{#faaAircraftRegistration.registrantRegion}, :#{#faaAircraftRegistration.countyMail}, " +
            ":#{#faaAircraftRegistration.countryMail}, :#{#faaAircraftRegistration.lastActivityDate}, " +
            ":#{#faaAircraftRegistration.certificateIssueDate}, :#{#faaAircraftRegistration.airworthinessClassificationCode}, " +
            ":#{#faaAircraftRegistration.approvedOperationCodes}, :#{#faaAircraftRegistration.typeAircraft}, " +
            ":#{#faaAircraftRegistration.typeEngine}, :#{#faaAircraftRegistration.statusCode}, " +
            ":#{#faaAircraftRegistration.modeSCode}, :#{#faaAircraftRegistration.fractionalOwnership}, " +
            ":#{#faaAircraftRegistration.airworthinessDate}, :#{#faaAircraftRegistration.otherName1}, " +
            ":#{#faaAircraftRegistration.otherName2}, :#{#faaAircraftRegistration.otherName3}, " +
            ":#{#faaAircraftRegistration.otherName4}, :#{#faaAircraftRegistration.otherName5}, " +
            ":#{#faaAircraftRegistration.expirationDate}, :#{#faaAircraftRegistration.uniqueId}, " +
            ":#{#faaAircraftRegistration.kitMfr}, :#{#faaAircraftRegistration.kitModel}, " +
            ":#{#faaAircraftRegistration.modeScodeHex}) " +
            "RETURNING *")
    Mono<FaaAircraftRegistration> insert(FaaAircraftRegistration faaAircraftRegistration);

    // Method for pagination
    Flux<FaaAircraftRegistration> findAllBy(Pageable pageable);

    // Custom query methods for specific fields
    Flux<FaaAircraftRegistration> findByEngineMfrModelCode(String engineMfrModelCode);
    Flux<FaaAircraftRegistration> findByTypeAircraft(String typeAircraft);
    Flux<FaaAircraftRegistration> findByAircraftMfrModelCode(String aircraftMfrModelCode);
    Flux<FaaAircraftRegistration> findByModeSCode(String modeSCode);
    Flux<FaaAircraftRegistration> findByModeScodeHex(String modeScodeHex);
}
