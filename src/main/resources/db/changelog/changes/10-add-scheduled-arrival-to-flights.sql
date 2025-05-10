-- liquibase formatted sql

-- changeset execodex:10
ALTER TABLE flights ADD scheduled_arrival TIMESTAMP;
