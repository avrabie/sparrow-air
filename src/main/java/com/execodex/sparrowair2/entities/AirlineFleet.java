package com.execodex.sparrowair2.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "airline_fleet")
public class AirlineFleet {
    @Id
    private Long id;

    @Column(value = "aircraft_type_icao")
    private String aircraftTypeIcao; // Reference to AircraftType.icaoCode

    @Column(value = "airline_icao")
    private String airlineIcao; // Reference to Airline.icaoCode

    @Column(value = "registration_number")
    private String registrationNumber; // Aircraft registration number (e.g., "N12345")

    @Column(value = "aircraft_age")
    private LocalDate aircraftAge; // Manufacture date of the aircraft

    @Column(value = "seat_configuration")
    private String seatConfiguration; // Description of seat layout (e.g., "3-3-3")

    @Column(value = "has_wifi")
    private Boolean hasWifi; // Flag for WiFi availability

    @Column(value = "has_power_outlets")
    private Boolean hasPowerOutlets; // Flag for power outlet availability

    @Column(value = "has_entertainment_system")
    private Boolean hasEntertainmentSystem; // Flag for in-flight entertainment system

    @Column(value = "first_class_seats")
    private Integer firstClassSeats; // Number of first class seats

    @Column(value = "business_seats")
    private Integer businessSeats; // Number of business class seats

    @Column(value = "economy_seats")
    private Integer economySeats; // Number of economy class seats

    @Column(value = "premium_economy_seats")
    private Integer premiumEconomySeats; // Number of premium economy class seats
}
