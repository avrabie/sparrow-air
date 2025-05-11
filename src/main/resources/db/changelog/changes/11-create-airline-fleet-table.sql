-- liquibase formatted sql

-- changeset execodex:11
CREATE TABLE airline_fleet (
    id BIGSERIAL PRIMARY KEY,
    aircraft_type_icao VARCHAR(4) NOT NULL,
    airline_icao VARCHAR(3) NOT NULL,
    aircraft_age DATE,
    seat_configuration VARCHAR(50),
    has_wifi BOOLEAN DEFAULT FALSE,
    has_power_outlets BOOLEAN DEFAULT FALSE,
    has_entertainment_system BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (aircraft_type_icao) REFERENCES aircraft_types(icao_code),
    FOREIGN KEY (airline_icao) REFERENCES airlines(icao_code)
);
