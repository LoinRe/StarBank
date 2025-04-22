-- liquibase formatted sql

-- changeset tatuka:1
CREATE TABLE IF NOT EXISTS dynamic_rule (
    id BIGSERIAL PRIMARY KEY,
    product_id VARCHAR(255),
    product_name VARCHAR(255),
    product_text VARCHAR(1000),
    rule JSONB
);