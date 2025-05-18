-- liquibase formatted sql

-- changeset execodex:08
CREATE TABLE booking_segments (
    id BIGSERIAL PRIMARY KEY,
    booking_id BIGINT NOT NULL,
    flight_id BIGINT NOT NULL,
    seat_id BIGINT NOT NULL,
    fare_class VARCHAR(40),
    ticket_number VARCHAR(60) UNIQUE,
    CONSTRAINT fk_booking_segment_booking FOREIGN KEY (booking_id) REFERENCES bookings(id),
    CONSTRAINT fk_booking_segment_flight FOREIGN KEY (flight_id) REFERENCES flights(id),
    CONSTRAINT fk_booking_segment_seat FOREIGN KEY (seat_id) REFERENCES seats(id)
);

-- Add a unique constraint to ensure a seat can only be booked once per flight
ALTER TABLE booking_segments ADD CONSTRAINT uk_flight_seat UNIQUE (flight_id, seat_id);