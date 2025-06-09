-- liquibase formatted sql

-- changeset execodex:16
CREATE TABLE countries (
    code VARCHAR(2) PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    capital VARCHAR(255),
    continent VARCHAR(255),
    currency VARCHAR(50),
    language VARCHAR(255)
);