-- liquibase formatted sql

-- changeset execodex:01
CREATE TABLE airlines (
    icao_code VARCHAR(3) PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    headquarters VARCHAR(255),
    contact_number VARCHAR(50),
    website VARCHAR(255)
);