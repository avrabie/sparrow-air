package com.execodex.sparrowair2.repositories;

import com.execodex.sparrowair2.entities.Passenger;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PassengerRepository extends ReactiveCrudRepository<Passenger, Long> {
    // The primary key of Passenger is id, which is a Long
    // ReactiveCrudRepository provides basic CRUD operations with reactive return types

    @Query("INSERT INTO passengers (first_name, last_name, date_of_birth, passport_number, nationality, email, phone) " +
            "VALUES (:#{#passenger.firstName}, :#{#passenger.lastName}, :#{#passenger.dateOfBirth}, :#{#passenger.passportNumber}, " +
            ":#{#passenger.nationality}, :#{#passenger.email}, :#{#passenger.phone}) " +
            "RETURNING *")
    Mono<Passenger> insert(Passenger passenger);

    // Find passengers by first name and last name
    Flux<Passenger> findByFirstNameAndLastName(String firstName, String lastName);

    Mono<Passenger> findByPassportNumber(String passportNumber);

    Flux<Passenger> findByDateOfBirth(String dateOfBirth);
}
