-- liquibase formatted sql

-- changeset execodex:13
ALTER TABLE airline_fleet ADD premium_economy_seats INT DEFAULT 0;
