-- liquibase formatted sql

-- changeset execodex:15
CREATE TABLE airports_new (
    icao_code VARCHAR(4) PRIMARY KEY NOT NULL,
    iata_code VARCHAR(4),
    name VARCHAR(255) NOT NULL,
    icao_region VARCHAR(50),
    icao_territory VARCHAR(50),
    location VARCHAR(255),
    city VARCHAR(255),
    country VARCHAR(255),
    elevation VARCHAR(50),
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    kc_code VARCHAR(50),
    airport_bs VARCHAR(255),
    airport_los VARCHAR(255),
    airport_re VARCHAR(255)
);