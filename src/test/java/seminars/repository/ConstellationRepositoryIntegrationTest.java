package seminars.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import seminars.constellation.SatelliteConstellation;
import seminars.satellite.CommunicationSatellite;
import seminars.satellite.ImagingSatellite;
import seminars.service.ConstellationService;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ConstellationRepository - Интеграционные тесты")
@SpringBootTest
class ConstellationRepositoryIntegrationTest {

    // Тестовые константы
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
            // Arrange
            SatelliteConstellation constellation = SatelliteConstellation.builder(CONSTELLATION_NAME_1).build();

            // Act - Create & Save
            repository.save(constellation);

            // Act - Read
            Optional<SatelliteConstellation> found = repository.findByName(CONSTELLATION_NAME_1);

            // Assert
            assertTrue(found.isPresent());
            assertEquals(CONSTELLATION_NAME_1, found.get().getConstellationName());
            assertTrue(repository.existsByName(CONSTELLATION_NAME_1));
            assertEquals(1, repository.count());
        }

        @Test
        @DisplayName("Полный цикл с добавлением спутников в группировку")
        void fullLifecycle_withSatellites_shouldPreserveData() {
            // Arrange
            SatelliteConstellation constellation = SatelliteConstellation.builder(CONSTELLATION_NAME_1).build();
            CommunicationSatellite satellite1 = new CommunicationSatellite(SATELLITE_NAME_1, ENERGY_LEVEL_1, BANDWIDTH);
            ImagingSatellite satellite2 = new ImagingSatellite(SATELLITE_NAME_2, ENERGY_LEVEL_2, RESOLUTION);

            // Act
            constellation.addSatellite(satellite1);
            constellation.addSatellite(satellite2);
            repository.save(constellation);

            // Assert
            Optional<SatelliteConstellation> found = repository.findByName(CONSTELLATION_NAME_1);
            assertTrue(found.isPresent());
            assertEquals(2, found.get().getSatellites().size());
            assertTrue(found.get().getSatellites().get(0) instanceof CommunicationSatellite);
            assertTrue(found.get().getSatellites().get(1) instanceof ImagingSatellite);
        }

        @Test
        @DisplayName("Полный цикл CRUD операций через репозиторий")
        void fullCrudCycle_shouldWorkCorrectly() {
            // Create
            SatelliteConstellation constellation1 = SatelliteConstellation.builder(CONSTELLATION_NAME_1).build();
            SatelliteConstellation constellation2 = SatelliteConstellation.builder(CONSTELLATION_NAME_2).build();
            repository.save(constellation1);
            repository.save(constellation2);

            // Read
            assertEquals(2, repository.count());
            assertTrue(repository.existsByName(CONSTELLATION_NAME_1));
            assertTrue(repository.existsByName(CONSTELLATION_NAME_2));

            // Update (save with same name)
            SatelliteConstellation updated = SatelliteConstellation.builder(CONSTELLATION_NAME_1).build();
            updated.addSatellite(new CommunicationSatellite(SATELLITE_NAME_1, ENERGY_LEVEL_1, BANDWIDTH));
            repository.save(updated);

            // Verify update
            Optional<SatelliteConstellation> found = repository.findByName(CONSTELLATION_NAME_1);
            assertTrue(found.isPresent());
            assertEquals(1, found.get().getSatellites().size());

            // Delete
            repository.deleteByName(CONSTELLATION_NAME_1);
            assertFalse(repository.existsByName(CONSTELLATION_NAME_1));
            assertEquals(1, repository.count());

            // Clear
            repository.clear();
            assertEquals(0, repository.count());
        }

        @Test
        @DisplayName("getAllConstellations должен возвращать все сохранённые группировки")
        void getAllConstellations_shouldReturnAllSaved() {
            // Arrange
            repository.save(SatelliteConstellation.builder(CONSTELLATION_NAME_1).build());
            repository.save(SatelliteConstellation.builder(CONSTELLATION_NAME_2).build());

            // Act
            Map<String, SatelliteConstellation> all = repository.getAllConstellations();

            // Assert
            assertEquals(2, all.size());
            assertTrue(all.containsKey(CONSTELLATION_NAME_1));
            assertTrue(all.containsKey(CONSTELLATION_NAME_2));
        }
    }

    @Nested
    @DisplayName("Интеграционные тесты сервиса с репозиторием")
    class ServiceIntegrationTests {

        @Test
        @DisplayName("Полный жизненный цикл через сервис: создание -> добавление спутников -> активация -> миссия")
        void fullLifecycle_throughService_shouldWorkCorrectly() {
            // Arrange
            CommunicationSatellite satellite = new CommunicationSatellite(SATELLITE_NAME_1, ENERGY_LEVEL_1, BANDWIDTH);

            // Act - Create constellation
            SatelliteConstellation constellation = service.createAndSaveConstellation(CONSTELLATION_NAME_1);

            // Verify creation
            assertTrue(repository.existsByName(CONSTELLATION_NAME_1));
            assertEquals(1, repository.count());

            // Act - Add satellite
            service.addSatelliteToConstellation(CONSTELLATION_NAME_1, satellite);

            // Verify satellite added
            Optional<SatelliteConstellation> found = repository.findByName(CONSTELLATION_NAME_1);
            assertTrue(found.isPresent());
            assertEquals(1, found.get().getSatellites().size());

            // Act - Activate satellites
            service.activateAllSatellites(CONSTELLATION_NAME_1);

            // Verify activation
            assertTrue(found.get().getSatellites().get(0).getState().isActive());

            // Act - Execute mission
            service.executeConstellationMission(CONSTELLATION_NAME_1);

            // Verify mission executed (energy consumed)
            assertTrue(found.get().getSatellites().get(0).getEnergy().getBatteryLevel() < ENERGY_LEVEL_1);
        }

        @Test
        @DisplayName("Создание нескольких группировок через сервис")
        void createMultipleConstellations_throughService_shouldWorkCorrectly() {
            // Act
            service.createAndSaveConstellation(CONSTELLATION_NAME_1);
            service.createAndSaveConstellation(CONSTELLATION_NAME_2);

            // Assert
            assertEquals(2, repository.count());
            assertTrue(service.constellationExists(CONSTELLATION_NAME_1));
            assertTrue(service.constellationExists(CONSTELLATION_NAME_2));
            assertEquals(2, service.getConstellationCount());
        }

        @Test
        @DisplayName("Добавление спутников в разные группировки через сервис")
        void addSatellitesToDifferentConstellations_throughService_shouldWorkCorrectly() {
            // Arrange
            service.createAndSaveConstellation(CONSTELLATION_NAME_1);
            service.createAndSaveConstellation(CONSTELLATION_NAME_2);
            CommunicationSatellite satellite1 = new CommunicationSatellite(SATELLITE_NAME_1, ENERGY_LEVEL_1, BANDWIDTH);
            ImagingSatellite satellite2 = new ImagingSatellite(SATELLITE_NAME_2, ENERGY_LEVEL_2, RESOLUTION);

            // Act
            service.addSatelliteToConstellation(CONSTELLATION_NAME_1, satellite1);
            service.addSatelliteToConstellation(CONSTELLATION_NAME_2, satellite2);

            // Assert
            Optional<SatelliteConstellation> found1 = repository.findByName(CONSTELLATION_NAME_1);
            Optional<SatelliteConstellation> found2 = repository.findByName(CONSTELLATION_NAME_2);

            assertTrue(found1.isPresent());
            assertTrue(found2.isPresent());
            assertEquals(1, found1.get().getSatellites().size());
            assertEquals(1, found2.get().getSatellites().size());
            assertTrue(found1.get().getSatellites().get(0) instanceof CommunicationSatellite);
            assertTrue(found2.get().getSatellites().get(0) instanceof ImagingSatellite);
        }

        @Test
        @DisplayName("Активация и выполнение миссий для группировки через сервис")
        void activateAndExecuteMission_throughService_shouldWorkCorrectly() {
            // Arrange
            service.createAndSaveConstellation(CONSTELLATION_NAME_1);
            CommunicationSatellite satellite = new CommunicationSatellite(SATELLITE_NAME_1, ENERGY_LEVEL_1, BANDWIDTH);
            service.addSatelliteToConstellation(CONSTELLATION_NAME_1, satellite);

            // Act - Check initial state
            Optional<SatelliteConstellation> found = repository.findByName(CONSTELLATION_NAME_1);
            assertFalse(found.get().getSatellites().get(0).getState().isActive());

            // Act - Activate
            service.activateAllSatellites(CONSTELLATION_NAME_1);

            // Assert - Check activated state
            found = repository.findByName(CONSTELLATION_NAME_1);
            assertTrue(found.get().getSatellites().get(0).getState().isActive());

            // Act - Execute mission
            double energyBefore = found.get().getSatellites().get(0).getEnergy().getBatteryLevel();
            service.executeConstellationMission(CONSTELLATION_NAME_1);

            // Assert - Check energy consumed
            found = repository.findByName(CONSTELLATION_NAME_1);
            double energyAfter = found.get().getSatellites().get(0).getEnergy().getBatteryLevel();
            assertTrue(energyAfter < energyBefore);
        }

        @Test
        @DisplayName("Показ статуса группировки через сервис")
        void showConstellationStatus_throughService_shouldWorkCorrectly() {
            // Arrange
            service.createAndSaveConstellation(CONSTELLATION_NAME_1);
            CommunicationSatellite satellite1 = new CommunicationSatellite(SATELLITE_NAME_1, ENERGY_LEVEL_1, BANDWIDTH);
            ImagingSatellite satellite2 = new ImagingSatellite(SATELLITE_NAME_2, ENERGY_LEVEL_2, RESOLUTION);
            service.addSatelliteToConstellation(CONSTELLATION_NAME_1, satellite1);
            service.addSatelliteToConstellation(CONSTELLATION_NAME_1, satellite2);

            // Act
            service.showConstellationStatus(CONSTELLATION_NAME_1);

            // Assert
            Optional<SatelliteConstellation> found = repository.findByName(CONSTELLATION_NAME_1);
            assertTrue(found.isPresent());
            assertEquals(2, found.get().getSatellites().size());
        }

        @Test
        @DisplayName("Операции с несуществующей группировкой через сервис")
        void operationsWithNonExistentConstellation_throughService_shouldHandleCorrectly() {
            // Act & Assert - Should not throw exceptions
            assertDoesNotThrow(() -> service.addSatelliteToConstellation("Несуществующая",
                    new CommunicationSatellite(SATELLITE_NAME_1, ENERGY_LEVEL_1, BANDWIDTH)));
            assertDoesNotThrow(() -> service.activateAllSatellites("Несуществующая"));
            assertDoesNotThrow(() -> service.executeConstellationMission("Несуществующая"));
            assertDoesNotThrow(() -> service.showConstellationStatus("Несуществующая"));
        }
    }

    @Nested
    @DisplayName("Тесты изоляции данных между тестами")
    class DataIsolationTests {

        @Test
        @DisplayName("Данные между тестами должны быть изолированы")
        void dataBetweenTests_shouldBeIsolated() {
            // Arrange
            repository.save(SatelliteConstellation.builder(CONSTELLATION_NAME_1).build());

            // Assert
            assertEquals(1, repository.count());
        }

        @Test
        @DisplayName("После setUp репозиторий должен быть пустым")
        void afterSetUp_repositoryShouldBeEmpty() {
            // Assert
            assertEquals(0, repository.count());
            assertTrue(repository.getAllConstellations().isEmpty());
        }
    }
}