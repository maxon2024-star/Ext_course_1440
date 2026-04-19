CREATE TABLE satellite_constellation (
                                         id BIGSERIAL PRIMARY KEY,
                                         constellation_name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE energy_system (
                               id BIGSERIAL PRIMARY KEY,
                               battery_level DOUBLE PRECISION NOT NULL
);

-- Базовая таблица спутников (состояние Embedded встроено сюда же)
CREATE TABLE satellite (
                           id BIGSERIAL PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           is_active BOOLEAN NOT NULL,
                           state_message VARCHAR(255),
                           energy_system_id BIGINT UNIQUE REFERENCES energy_system(id),
                           constellation_id BIGINT REFERENCES satellite_constellation(id)
);

-- Индексы для оптимизации поиска (по имени и по группировке)
CREATE INDEX idx_satellite_name ON satellite(name);
CREATE INDEX idx_satellite_constellation_id ON satellite(constellation_id);

-- Таблицы-наследники (стратегия JOINED)
CREATE TABLE communication_satellite (
                                         id BIGINT PRIMARY KEY REFERENCES satellite(id),
                                         bandwidth DOUBLE PRECISION NOT NULL
);

CREATE TABLE imaging_satellite (
                                   id BIGINT PRIMARY KEY REFERENCES satellite(id),
                                   photos_taken INT NOT NULL,
                                   resolution DOUBLE PRECISION NOT NULL
);