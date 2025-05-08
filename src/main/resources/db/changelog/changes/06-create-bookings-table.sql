-- liquibase formatted sql

-- changeset execodex:06
CREATE TABLE bookings (
    id BIGSERIAL PRIMARY KEY,
    booking_reference VARCHAR(10) NOT NULL UNIQUE,
    passenger_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_booking_passenger FOREIGN KEY (passenger_id) REFERENCES passengers(id)
);