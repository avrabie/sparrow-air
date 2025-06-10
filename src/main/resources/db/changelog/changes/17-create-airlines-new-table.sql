-- liquibase formatted sql

-- changeset execodex:17
CREATE TABLE airlines_new (
    airline_id INT PRIMARY KEY NOT NULL,
    name VARCHAR(255),
    alias VARCHAR(255),
    iata VARCHAR(3),
    icao_code VARCHAR(4),
    callsign VARCHAR(255),
    country VARCHAR(255),
    active VARCHAR(1)
);