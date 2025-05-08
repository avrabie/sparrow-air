-- liquibase formatted sql

-- changeset execodex:07
CREATE TABLE seats (
    id BIGSERIAL PRIMARY KEY,
    flight_id BIGINT NOT NULL,
    seat_number VARCHAR(10) NOT NULL,
    class VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_seat_flight FOREIGN KEY (flight_id) REFERENCES flights(id)
);

-- Add a unique constraint to ensure each seat number is unique per flight
ALTER TABLE seats ADD CONSTRAINT uk_flight_seat_number UNIQUE (flight_id, seat_number);