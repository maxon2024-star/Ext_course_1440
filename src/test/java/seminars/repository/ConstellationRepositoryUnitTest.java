package seminars.repository;

import org.junit.jupiter.api.*;
import seminars.constellation.SatelliteConstellation;
import seminars.satellite.CommunicationSatellite;
import seminars.satellite.ImagingSatellite;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ConstellationRepository - Юнит тесты")
class ConstellationRepositoryUnitTest {

    // Тестовые константы
    private static final String CONSTELLATION_NAME_1 = "Орбита-1";
    private static final String CONSTELLATION_NAME_2 = "Орбита-2";
    private static final String CONSTELLATION_NAME_NONEXISTENT = "Несуществующая";
    private static final String SATELLITE_NAME_1 = "Связь-1";
    private static final String SATELLITE_NAME_2 = "ДЗЗ-1";
    private static final double ENERGY_LEVEL_1 = 0.85;
    private static final double ENERGY_LEVEL_2 = 0.92;
    private static final double BANDWIDTH = 500.0;
    private static final double RESOLUTION = 2.5;

    private ConstellationRepository repository;

    @BeforeEach
    @DisplayName("Инициализация репозитория перед каждым тестом")
    void setUp() {
        repository = new ConstellationRepository();
    }

    @Nested
    @DisplayName("Тесты метода save()")
    class SaveTests {

        @Test
        @DisplayName("Сохранение новой группировки должно успешно добавлять её в репозиторий")
        void save_newConstellation_shouldAddToRepository() {
            // Arrange
            SatelliteConstellation constellation = SatelliteConstellation.builder(CONSTELLATION_NAME_1).build();

            // Act
            repository.save(constellation);

            // Assert
            assertTrue(repository.existsByName(CONSTELLATION_NAME_1));
            assertEquals(1, repository.count());
        }

        @Test
        @DisplayName("Сохранение группировки с тем же именем должно обновлять существующую")
        void save_existingConstellationName_shouldUpdateExisting() {
            // Arrange
            SatelliteConstellation constellation1 = SatelliteConstellation.builder(CONSTELLATION_NAME_1).build();
            SatelliteConstellation constellation2 = SatelliteConstellation.builder(CONSTELLATION_NAME_1).build();
            ImagingSatellite satellite = new ImagingSatellite(SATELLITE_NAME_1, ENERGY_LEVEL_1, RESOLUTION);
            constellation2.addSatellite(satellite);

            // Act
            repository.save(constellation1);
            repository.save(constellation2);

            // Assert
            Optional<SatelliteConstellation> found = repository.findByName(CONSTELLATION_NAME_1);
            assertTrue(found.isPresent());
            assertEquals(1, found.get().getSatellites().size());
        }

        @Test
        @DisplayName("Сохранение нескольких группировок должно увеличивать счётчик")
        void save_multipleConstellations_shouldIncreaseCount() {
            // Arrange
            SatelliteConstellation constellation1 = SatelliteConstellation.builder(CONSTELLATION_NAME_1).build();
            SatelliteConstellation constellation2 = SatelliteConstellation.builder(CONSTELLATION_NAME_2).build();

            // Act
            repository.save(constellation1);
            repository.save(constellation2);

            // Assert
            assertEquals(2, repository.count());
            assertTrue(repository.existsByName(CONSTELLATION_NAME_1));
            assertTrue(repository.existsByName(CONSTELLATION_NAME_2));
        }
    }

    @Nested
    @DisplayName("Тесты метода findByName()")
    class FindByNameTests {

        @Test
        @DisplayName("Поиск существующей группировки должен возвращать Optional с значением")
        void findByName_existingConstellation_shouldReturnPresentOptional() {
            // Arrange
            SatelliteConstellation constellation = SatelliteConstellation.builder(CONSTELLATION_NAME_1).build();
            repository.save(constellation);

            // Act
            Optional<SatelliteConstellation> found = repository.findByName(CONSTELLATION_NAME_1);

            // Assert
            assertTrue(found.isPresent());
            assertEquals(CONSTELLATION_NAME_1, found.get().getConstellationName());
        }

        @Test
        @DisplayName("Поиск несуществующей группировки должен возвращать пустой Optional")
        void findByName_nonExistentConstellation_shouldReturnEmptyOptional() {
            // Act
            Optional<SatelliteConstellation> found = repository.findByName(CONSTELLATION_NAME_NONEXISTENT);

            // Assert
            assertTrue(found.isEmpty());
        }

        @Test
        @DisplayName("Поиск по null имени должен возвращать пустой Optional")
        void findByName_nullName_shouldReturnEmptyOptional() {
            // Act
            Optional<SatelliteConstellation> found = repository.findByName(null);

            // Assert
            assertTrue(found.isEmpty());
        }
    }

    @Nested
    @DisplayName("Тесты метода getAllConstellations()")
    class GetAllConstellationsTests {

        @Test
        @DisplayName("Получение всех группировок из пустого репозитория должно возвращать пустую карту")
        void getAllConstellations_emptyRepository_shouldReturnEmptyMap() {
            // Act
            Map<String, SatelliteConstellation> all = repository.getAllConstellations();

            // Assert
            assertTrue(all.isEmpty());
            assertEquals(0, all.size());
        }

        @Test
        @DisplayName("Получение всех группировок должно возвращать копию данных")
        void getAllConstellations_withData_shouldReturnCopy() {
            // Arrange
            SatelliteConstellation constellation = SatelliteConstellation.builder(CONSTELLATION_NAME_1).build();
            repository.save(constellation);

            // Act
            Map<String, SatelliteConstellation> all = repository.getAllConstellations();

            // Assert
            assertEquals(1, all.size());
            assertTrue(all.containsKey(CONSTELLATION_NAME_1));

            // Модификация возвращённой карты не должна влиять на репозиторий
            all.clear();
            assertEquals(1, repository.count());
        }

        @Test
        @DisplayName("Получение всех группировок должно возвращать все сохранённые группировки")
        void getAllConstellations_multipleConstellations_shouldReturnAll() {
            // Arrange
            SatelliteConstellation constellation1 = SatelliteConstellation.builder(CONSTELLATION_NAME_1).build();
            SatelliteConstellation constellation2 = SatelliteConstellation.builder(CONSTELLATION_NAME_2).build();
            repository.save(constellation1);
            repository.save(constellation2);

            // Act
            Map<String, SatelliteConstellation> all = repository.getAllConstellations();

            // Assert
            assertEquals(2, all.size());
            assertTrue(all.containsKey(CONSTELLATION_NAME_1));
            assertTrue(all.containsKey(CONSTELLATION_NAME_2));
        }
    }

    @Nested
    @DisplayName("Тесты метода existsByName()")
    class ExistsByNameTests {

        @Test
        @DisplayName("Проверка существования сохранённой группировки должна возвращать true")
        void existsByName_savedConstellation_shouldReturnTrue() {
            // Arrange
            SatelliteConstellation constellation = SatelliteConstellation.builder(CONSTELLATION_NAME_1).build();
            repository.save(constellation);

            // Act
            boolean exists = repository.existsByName(CONSTELLATION_NAME_1);

            // Assert
            assertTrue(exists);
        }

        @Test
        @DisplayName("Проверка существования несуществующей группировки должна возвращать false")
        void existsByName_nonExistentConstellation_shouldReturnFalse() {
            // Act
            boolean exists = repository.existsByName(CONSTELLATION_NAME_NONEXISTENT);

            // Assert
            assertFalse(exists);
        }

        @Test
        @DisplayName("Проверка существования после удаления должна возвращать false")
        void existsByName_afterDelete_shouldReturnFalse() {
            // Arrange
            SatelliteConstellation constellation = SatelliteConstellation.builder(CONSTELLATION_NAME_1).build();
            repository.save(constellation);
            repository.deleteByName(CONSTELLATION_NAME_1);

            // Act
            boolean exists = repository.existsByName(CONSTELLATION_NAME_1);

            // Assert
            assertFalse(exists);
        }
    }

    @Nested
    @DisplayName("Тесты метода deleteByName()")
    class DeleteByNameTests {

        @Test
        @DisplayName("Удаление существующей группировки должно уменьшать счётчик")
        void deleteByName_existingConstellation_shouldDecreaseCount() {
            // Arrange
            SatelliteConstellation constellation = SatelliteConstellation.builder(CONSTELLATION_NAME_1).build();
            repository.save(constellation);

            // Act
            repository.deleteByName(CONSTELLATION_NAME_1);

            // Assert
            assertEquals(0, repository.count());
            assertFalse(repository.existsByName(CONSTELLATION_NAME_1));
        }

        @Test
        @DisplayName("Удаление несуществующей группировки не должно вызывать ошибок")
        void deleteByName_nonExistentConstellation_shouldNotThrowException() {
            // Act & Assert
            assertDoesNotThrow(() -> repository.deleteByName(CONSTELLATION_NAME_NONEXISTENT));
        }
    }

    @Nested
    @DisplayName("Тесты метода count()")
    class CountTests {

        @Test
        @DisplayName("Счётчик пустого репозитория должен быть равен 0")
        void count_emptyRepository_shouldReturnZero() {
            // Act
            int count = repository.count();

            // Assert
            assertEquals(0, count);
        }

        @Test
        @DisplayName("Счётчик должен соответствовать количеству сохранённых группировок")
        void count_withMultipleConstellations_shouldReturnCorrectCount() {
            // Arrange
            repository.save(SatelliteConstellation.builder(CONSTELLATION_NAME_1).build());
            repository.save(SatelliteConstellation.builder(CONSTELLATION_NAME_2).build());
            repository.save(SatelliteConstellation.builder("Орбита-3").build());

            // Act
            int count = repository.count();

            // Assert
            assertEquals(3, count);
        }
    }

    @Nested
    @DisplayName("Тесты метода clear()")
    class ClearTests {

        @Test
        @DisplayName("Очистка репозитория должна удалять все группировки")
        void clear_withData_shouldRemoveAllConstellations() {
            // Arrange
            repository.save(SatelliteConstellation.builder(CONSTELLATION_NAME_1).build());
            repository.save(SatelliteConstellation.builder(CONSTELLATION_NAME_2).build());

            // Act
            repository.clear();

            // Assert
            assertEquals(0, repository.count());
            assertTrue(repository.getAllConstellations().isEmpty());
        }

        @Test
        @DisplayName("Очистка пустого репозитория не должна вызывать ошибок")
        void clear_emptyRepository_shouldNotThrowException() {
            // Act & Assert
            assertDoesNotThrow(() -> repository.clear());
        }
    }

    @Nested
    @DisplayName("Граничные тесты")
    class BoundaryTests {

        @Test
        @DisplayName("Сохранение группировки с пустым именем должно работать")
        void save_emptyNameConstellation_shouldWork() {
            // Arrange
            SatelliteConstellation constellation = SatelliteConstellation.builder("").build();

            // Act & Assert
            assertDoesNotThrow(() -> repository.save(constellation));
            assertTrue(repository.existsByName(""));
        }

        @Test
        @DisplayName("Сохранение группировки с очень длинным именем должно работать")
        void save_longNameConstellation_shouldWork() {
            // Arrange
            String longName = "О".repeat(1000);
            SatelliteConstellation constellation = SatelliteConstellation.builder(longName).build();

            // Act & Assert
            assertDoesNotThrow(() -> repository.save(constellation));
            assertTrue(repository.existsByName(longName));
        }

        @Test
        @DisplayName("Добавление спутников в группировку перед сохранением должно сохраняться")
        void save_constellationWithSatellites_shouldPreserveSatellites() {
            // Arrange
            SatelliteConstellation constellation = SatelliteConstellation.builder(CONSTELLATION_NAME_1).build();
            CommunicationSatellite satellite1 = new CommunicationSatellite(SATELLITE_NAME_1, ENERGY_LEVEL_1, BANDWIDTH);
            ImagingSatellite satellite2 = new ImagingSatellite(SATELLITE_NAME_2, ENERGY_LEVEL_2, RESOLUTION);
            constellation.addSatellite(satellite1);
            constellation.addSatellite(satellite2);

            // Act
            repository.save(constellation);
            Optional<SatelliteConstellation> found = repository.findByName(CONSTELLATION_NAME_1);

            // Assert
            assertTrue(found.isPresent());
            assertEquals(2, found.get().getSatellites().size());
        }
    }
}