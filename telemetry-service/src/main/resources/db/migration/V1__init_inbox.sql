CREATE TABLE inbox (
                       event_id UUID PRIMARY KEY,
                       aggregate_id VARCHAR(255) NOT NULL,
                       event_type VARCHAR(50) NOT NULL,
                       processed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);