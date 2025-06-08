-- liquibase formatted sql

-- changeset execodex:02
CREATE TABLE airport2s (
    icao_code VARCHAR(4) PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    city VARCHAR(255),
    country VARCHAR(255),
    timezone VARCHAR(50),
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION
);