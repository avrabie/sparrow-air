-- liquibase formatted sql

-- changeset execodex:12
-- Add airline_fleet_id column to flights table
ALTER TABLE flights ADD COLUMN airline_fleet_id BIGINT;

-- Drop the foreign key constraint for aircraft_type_icao
ALTER TABLE flights DROP CONSTRAINT fk_flight_aircraft_type;

-- Drop the aircraft_type_icao column
ALTER TABLE flights DROP COLUMN aircraft_type_icao;

-- Add foreign key constraint for airline_fleet_id
ALTER TABLE flights ADD CONSTRAINT fk_flight_airline_fleet FOREIGN KEY (airline_fleet_id) REFERENCES airline_fleet(id);

-- Make airline_fleet_id NOT NULL after migration
ALTER TABLE flights ALTER COLUMN airline_fleet_id SET NOT NULL;