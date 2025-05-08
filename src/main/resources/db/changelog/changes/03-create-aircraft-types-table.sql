-- liquibase formatted sql

-- changeset execodex:03
CREATE TABLE aircraft_types (
    icao_code VARCHAR(4) PRIMARY KEY NOT NULL,
    model_name VARCHAR(255) NOT NULL,
    manufacturer VARCHAR(255),
    seating_capacity INT,
    max_range_km INT
);