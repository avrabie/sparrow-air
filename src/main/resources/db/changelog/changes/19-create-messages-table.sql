-- liquibase formatted sql

-- changeset execodex:19
CREATE TABLE messages (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    subject VARCHAR(255) NOT NULL,
    message VARCHAR(4000) NOT NULL,
    ip_address VARCHAR(45) NOT NULL
);
