CREATE TABLE outbox (
                        id UUID PRIMARY KEY,
                        aggregate_id VARCHAR(255) NOT NULL,
                        event_type VARCHAR(50) NOT NULL,
                        payload JSONB NOT NULL,
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        status VARCHAR(20) NOT NULL
);

-- Индекс для быстрого поиска необработанных событий планировщиком
CREATE INDEX idx_outbox_status ON outbox(status) WHERE status = 'PENDING';