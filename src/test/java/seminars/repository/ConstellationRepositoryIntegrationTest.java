package seminars.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import seminars.constellation.SatelliteConstellation;
import seminars.satellite.CommunicationSatellite;
import seminars.satellite.ImagingSatellite;
import seminars.service.ConstellationService;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ConstellationRepository - Интеграционные тесты")
@SpringBootTest
@Transactional // ВАЖНО: держит сессию БД открытой во время всего теста (решает проблему LazyInitialization)
class ConstellationRepositoryIntegrationTest {

    private static final String CONSTELLATION_NAME_1 = "Орбита-Интеграция-1";
    private static final String CONSTELLATION_NAME_2 = "Орбита-Интеграция-2";
    private static final String SATELLITE_NAME_1 = "Спутник-Тест-1";
    private static final String SATELLITE_NAME_2 = "Спутник-Тест-2";
    private static final double ENERGY_LEVEL_1 = 0.90;
    private static final double ENERGY_LEVEL_2 = 0.85;
    private static final double BANDWIDTH = 600.0;
    private static final double RESOLUTION = 3.0;

    @Autowired
    private ConstellationRepository repository;

    @Autowired
    private ConstellationService service;

    @BeforeEach
    @DisplayName("Очистка репозитория перед каждым тестом")
    void setUp() {
        repository.clear();
    }

    @Nested
    @DisplayName("Интеграционные тесты репозитория")
    class RepositoryIntegrationTests {

        @Test
        @DisplayName("Полный цикл: создание группировки -> сохранение -> поиск -> проверка")
        void fullLifecycle_createSaveFindVerify_shouldWorkCorrectly() {
            SatelliteConstellation constellation = SatelliteConstellation.builder(CONSTELLATION_NAME_1).build();
            repository.save(constellation);
            Optional<SatelliteConstellation> found = repository.findByName(CONSTELLATION_NAME_1);

            assertTrue(found.isPresent());
            assertEquals(CONSTELLATION_NAME_1, found.get().getConstellationName());
            assertTrue(repository.existsByName(CONSTELLATION_NAME_1));
            assertEquals(1, repository.count());
        }

        @Test
        @DisplayName("Полный цикл с добавлением спутников в группировку")
        void fullLifecycle_withSatellites_shouldPreserveData() {
            SatelliteConstellation constellation = SatelliteConstellation.builder(CONSTELLATION_NAME_1).build();
            CommunicationSatellite satellite1 = new CommunicationSatellite(SATELLITE_NAME_1, ENERGY_LEVEL_1, BANDWIDTH);
            ImagingSatellite satellite2 = new ImagingSatellite(SATELLITE_NAME_2, ENERGY_LEVEL_2, RESOLUTION);

            constellation.addSatellite(satellite1);
            constellation.addSatellite(satellite2);
            repository.save(constellation);

            Optional<SatelliteConstellation> found = repository.findByName(CONSTELLATION_NAME_1);
            assertTrue(found.isPresent());
            assertEquals(2, found.get().getSatellites().size());
        }

        @Test
        @DisplayName("Полный цикл CRUD операций через репозиторий")
        void fullCrudCycle_shouldWorkCorrectly() {
            SatelliteConstellation constellation1 = SatelliteConstellation.builder(CONSTELLATION_NAME_1).build();
            SatelliteConstellation constellation2 = SatelliteConstellation.builder(CONSTELLATION_NAME_2).build();
            repository.save(constellation1);
            repository.save(constellation2);

            assertEquals(2, repository.count());

            // ИСПРАВЛЕНИЕ: Достаем сущность из БД перед апдейтом
            SatelliteConstellation updated = repository.findByName(CONSTELLATION_NAME_1).orElseThrow();
            updated.addSatellite(new CommunicationSatellite(SATELLITE_NAME_1, ENERGY_LEVEL_1, BANDWIDTH));
            repository.save(updated);

            Optional<SatelliteConstellation> found = repository.findByName(CONSTELLATION_NAME_1);
            assertTrue(found.isPresent());
            assertEquals(1, found.get().getSatellites().size());

            repository.deleteByName(CONSTELLATION_NAME_1);
            assertFalse(repository.existsByName(CONSTELLATION_NAME_1));
            assertEquals(1, repository.count());

            repository.clear();
            assertEquals(0, repository.count());
        }

        @Test
        @DisplayName("getAllConstellations должен возвращать все сохранённые группировки")
        void getAllConstellations_shouldReturnAllSaved() {
            repository.save(SatelliteConstellation.builder(CONSTELLATION_NAME_1).build());
            repository.save(SatelliteConstellation.builder(CONSTELLATION_NAME_2).build());

            Map<String, SatelliteConstellation> all = repository.getAllConstellations();

            assertEquals(2, all.size());
            assertTrue(all.containsKey(CONSTELLATION_NAME_1));
            assertTrue(all.containsKey(CONSTELLATION_NAME_2));
        }
    }

    @Nested
    @DisplayName("Интеграционные тесты сервиса с репозиторием")
    class ServiceIntegrationTests {
        // Остальные тесты внутри ServiceIntegrationTests и DataIsolationTests остаются без изменений,
        // они теперь заработают благодаря @Transactional над классом.

        @Test
        void fullLifecycle_throughService_shouldWorkCorrectly() {
            CommunicationSatellite satellite = new CommunicationSatellite(SATELLITE_NAME_1, ENERGY_LEVEL_1, BANDWIDTH);
            service.createAndSaveConstellation(CONSTELLATION_NAME_1);
            service.addSatelliteToConstellation(CONSTELLATION_NAME_1, satellite);
            service.activateAllSatellites(CONSTELLATION_NAME_1);

            Optional<SatelliteConstellation> found = repository.findByName(CONSTELLATION_NAME_1);
            assertTrue(found.get().getSatellites().get(0).getState().isActive());

            service.executeConstellationMission(CONSTELLATION_NAME_1);
            assertTrue(found.get().getSatellites().get(0).getEnergy().getBatteryLevel() < ENERGY_LEVEL_1);
        }

        @Test
        void createMultipleConstellations_throughService_shouldWorkCorrectly() {
            service.createAndSaveConstellation(CONSTELLATION_NAME_1);
            service.createAndSaveConstellation(CONSTELLATION_NAME_2);
            assertEquals(2, repository.count());
        }

        @Test
        void addSatellitesToDifferentConstellations_throughService_shouldWorkCorrectly() {
            service.createAndSaveConstellation(CONSTELLATION_NAME_1);
            service.createAndSaveConstellation(CONSTELLATION_NAME_2);
            service.addSatelliteToConstellation(CONSTELLATION_NAME_1, new CommunicationSatellite(SATELLITE_NAME_1, ENERGY_LEVEL_1, BANDWIDTH));
            service.addSatelliteToConstellation(CONSTELLATION_NAME_2, new ImagingSatellite(SATELLITE_NAME_2, ENERGY_LEVEL_2, RESOLUTION));

            assertEquals(1, repository.findByName(CONSTELLATION_NAME_1).get().getSatellites().size());
            assertEquals(1, repository.findByName(CONSTELLATION_NAME_2).get().getSatellites().size());
        }

        @Test
        void activateAndExecuteMission_throughService_shouldWorkCorrectly() {
            service.createAndSaveConstellation(CONSTELLATION_NAME_1);
            service.addSatelliteToConstellation(CONSTELLATION_NAME_1, new CommunicationSatellite(SATELLITE_NAME_1, ENERGY_LEVEL_1, BANDWIDTH));
            service.activateAllSatellites(CONSTELLATION_NAME_1);

            Optional<SatelliteConstellation> found = repository.findByName(CONSTELLATION_NAME_1);
            double energyBefore = found.get().getSatellites().get(0).getEnergy().getBatteryLevel();
            service.executeConstellationMission(CONSTELLATION_NAME_1);
            assertTrue(found.get().getSatellites().get(0).getEnergy().getBatteryLevel() < energyBefore);
        }

        @Test
        void showConstellationStatus_throughService_shouldWorkCorrectly() {
            service.createAndSaveConstellation(CONSTELLATION_NAME_1);
            service.addSatelliteToConstellation(CONSTELLATION_NAME_1, new CommunicationSatellite(SATELLITE_NAME_1, ENERGY_LEVEL_1, BANDWIDTH));
            assertDoesNotThrow(() -> service.showConstellationStatus(CONSTELLATION_NAME_1));
        }

        @Test
        void operationsWithNonExistentConstellation_throughService_shouldHandleCorrectly() {
            assertDoesNotThrow(() -> service.addSatelliteToConstellation("Несуществующая", new CommunicationSatellite(SATELLITE_NAME_1, 1, 1)));
        }
    }
}