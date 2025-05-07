package com.execodex.sparrowair2.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("flights")
public class Flight {
    @Id
    private Long id;

    @Column("airline_icao_code")
    private String airlineIcaoCode;

    @Column("flight_number")
    private String flightNumber;

    @Column("departure_airport_icao")
    private String departureAirportIcao;

    @Column("arrival_airport_icao")
    private String arrivalAirportIcao;

    @Column("scheduled_departure")
    private LocalDateTime scheduledDeparture;

    @Column("aircraft_type_icao")
    private String aircraftTypeIcao;

    @Column("status")
    private String status;


}