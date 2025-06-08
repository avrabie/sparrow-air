-- liquibase formatted sql

-- changeset execodex:04
CREATE TABLE flights (
    id BIGSERIAL PRIMARY KEY,
    airline_icao_code VARCHAR(3) NOT NULL,
    flight_number VARCHAR(10) NOT NULL,
    departure_airport_icao VARCHAR(4) NOT NULL,
    arrival_airport_icao VARCHAR(4) NOT NULL,
    scheduled_departure TIMESTAMP NOT NULL,
    aircraft_type_icao VARCHAR(4) NOT NULL,
    status VARCHAR(20),
    CONSTRAINT fk_flight_airline FOREIGN KEY (airline_icao_code) REFERENCES airlines(icao_code),
    CONSTRAINT fk_flight_departure_airport FOREIGN KEY (departure_airport_icao) REFERENCES airports(icao_code),
    CONSTRAINT fk_flight_arrival_airport FOREIGN KEY (arrival_airport_icao) REFERENCES airports(icao_code),
    CONSTRAINT fk_flight_aircraft_type FOREIGN KEY (aircraft_type_icao) REFERENCES aircraft(icao_code)
);