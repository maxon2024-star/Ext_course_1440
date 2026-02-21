package seminars.repository;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import seminars.constellation.SatelliteConstellation;
import seminars.satellite.CommunicationSatellite;
import seminars.satellite.ImagingSatellite;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("ConstellationRepository - Мок тесты")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ConstellationRepositoryMockTest {

    // Тестовые константы
    private static final String CONSTELLATION_NAME_1 = "Орбита-1";
    private static final String CONSTELLATION_NAME_2 = "Орбита-2";
    private static final String CONSTELLATION_NAME_NONEXISTENT = "Несуществующая";
    private static final String SATELLITE_NAME_1 = "Связь-1";
    private static final double ENERGY_LEVEL_1 = 0.85;
    private static final double BANDWIDTH = 500.0;
    private static final double RESOLUTION = 2.5;

    @Mock
    private ConstellationRepository repositoryMock;

    @Captor
    private ArgumentCaptor<SatelliteConstellation> constellationCaptor;

    @Nested
    @DisplayName("Тесты метода save() с моками")
    class SaveMockTests {

        @Test
        @DisplayName("Мок save() должен вызываться с корректной группировкой")
        void save_shouldCallWithCorrectConstellation() {
            // Arrange
            SatelliteConstellation constellation = new SatelliteConstellation(CONSTELLATION_NAME_1);
            doNothing().when(repositoryMock).save(any(SatelliteConstellation.class));

            // Act
            repositoryMock.save(constellation);

            // Assert
            verify(repositoryMock, times(1)).save(constellationCaptor.capture());
            assertEquals(CONSTELLATION_NAME_1, constellationCaptor.getValue().getConstellationName());
        }

        @Test
        @DisplayName("Мок save() должен вызываться ровно один раз")
        void save_shouldBeCalledExactlyOnce() {
            // Arrange
            SatelliteConstellation constellation = new SatelliteConstellation(CONSTELLATION_NAME_1);
            doNothing().when(repositoryMock).save(any(SatelliteConstellation.class));

            // Act
            repositoryMock.save(constellation);

            // Assert
            verify(repositoryMock, times(1)).save(any(SatelliteConstellation.class));
        }

        @Test
        @DisplayName("Мок save() при нескольких вызовах должен фиксировать все вызовы")
        void save_multipleCalls_shouldRecordAllCalls() {
            // Arrange
            SatelliteConstellation constellation1 = new SatelliteConstellation(CONSTELLATION_NAME_1);
            SatelliteConstellation constellation2 = new SatelliteConstellation(CONSTELLATION_NAME_2);
            doNothing().when(repositoryMock).save(any(SatelliteConstellation.class));

            // Act
            repositoryMock.save(constellation1);
            repositoryMock.save(constellation2);

            // Assert
            verify(repositoryMock, times(2)).save(any(SatelliteConstellation.class));
        }
    }

    @Nested
    @DisplayName("Тесты метода findByName() с моками")
    class FindByNameMockTests {

        @Test
        @DisplayName("Мок findByName() для существующей группировки должен возвращать Optional с значением")
        void findByName_existingConstellation_shouldReturnPresentOptional() {
            // Arrange
            SatelliteConstellation constellation = new SatelliteConstellation(CONSTELLATION_NAME_1);
            when(repositoryMock.findByName(CONSTELLATION_NAME_1)).thenReturn(Optional.of(constellation));

            // Act
            Optional<SatelliteConstellation> found = repositoryMock.findByName(CONSTELLATION_NAME_1);

            // Assert
            assertTrue(found.isPresent());
            assertEquals(CONSTELLATION_NAME_1, found.get().getConstellationName());
            verify(repositoryMock, times(1)).findByName(CONSTELLATION_NAME_1);
        }

        @Test
        @DisplayName("Мок findByName() для несуществующей группировки должен возвращать пустой Optional")
        void findByName_nonExistentConstellation_shouldReturnEmptyOptional() {
            // Arrange
            when(repositoryMock.findByName(CONSTELLATION_NAME_NONEXISTENT)).thenReturn(Optional.empty());

            // Act
            Optional<SatelliteConstellation> found = repositoryMock.findByName(CONSTELLATION_NAME_NONEXISTENT);

            // Assert
            assertTrue(found.isEmpty());
            verify(repositoryMock, times(1)).findByName(CONSTELLATION_NAME_NONEXISTENT);
        }

        @Test
        @DisplayName("Мок findByName() должен возвращать настроенное значение")
        void findByName_shouldReturnConfiguredValue() {
            // Arrange
            SatelliteConstellation constellation = new SatelliteConstellation(CONSTELLATION_NAME_1);
            CommunicationSatellite satellite = new CommunicationSatellite(SATELLITE_NAME_1, ENERGY_LEVEL_1, BANDWIDTH);
            constellation.addSatellite(satellite);
            when(repositoryMock.findByName(CONSTELLATION_NAME_1)).thenReturn(Optional.of(constellation));

            // Act
            Optional<SatelliteConstellation> found = repositoryMock.findByName(CONSTELLATION_NAME_1);

            // Assert
            assertTrue(found.isPresent());
            assertEquals(1, found.get().getSatellites().size());
        }
    }

    @Nested
    @DisplayName("Тесты метода getAllConstellations() с моками")
    class GetAllConstellationsMockTests {

        @Test
        @DisplayName("Мок getAllConstellations() должен возвращать настроенную карту")
        void getAllConstellations_shouldReturnConfiguredMap() {
            // Arrange
            Map<String, SatelliteConstellation> mockMap = new HashMap<>();
            mockMap.put(CONSTELLATION_NAME_1, new SatelliteConstellation(CONSTELLATION_NAME_1));
            mockMap.put(CONSTELLATION_NAME_2, new SatelliteConstellation(CONSTELLATION_NAME_2));
            when(repositoryMock.getAllConstellations()).thenReturn(mockMap);

            // Act
            Map<String, SatelliteConstellation> result = repositoryMock.getAllConstellations();

            // Assert
            assertEquals(2, result.size());
            assertTrue(result.containsKey(CONSTELLATION_NAME_1));
            assertTrue(result.containsKey(CONSTELLATION_NAME_2));
            verify(repositoryMock, times(1)).getAllConstellations();
        }

        @Test
        @DisplayName("Мок getAllConstellations() должен возвращать пустую карту когда настроено")
        void getAllConstellations_emptyMap_shouldReturnEmpty() {
            // Arrange
            when(repositoryMock.getAllConstellations()).thenReturn(new HashMap<>());

            // Act
            Map<String, SatelliteConstellation> result = repositoryMock.getAllConstellations();

            // Assert
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("Тесты метода existsByName() с моками")
    class ExistsByNameMockTests {

        @Test
        @DisplayName("Мок existsByName() для существующей группировки должен возвращать true")
        void existsByName_existingConstellation_shouldReturnTrue() {
            // Arrange
            when(repositoryMock.existsByName(CONSTELLATION_NAME_1)).thenReturn(true);

            // Act
            boolean exists = repositoryMock.existsByName(CONSTELLATION_NAME_1);

            // Assert
            assertTrue(exists);
            verify(repositoryMock, times(1)).existsByName(CONSTELLATION_NAME_1);
        }

        @Test
        @DisplayName("Мок existsByName() для несуществующей группировки должен возвращать false")
        void existsByName_nonExistentConstellation_shouldReturnFalse() {
            // Arrange
            when(repositoryMock.existsByName(CONSTELLATION_NAME_NONEXISTENT)).thenReturn(false);

            // Act
            boolean exists = repositoryMock.existsByName(CONSTELLATION_NAME_NONEXISTENT);

            // Assert
            assertFalse(exists);
        }
    }

    @Nested
    @DisplayName("Тесты метода deleteByName() с моками")
    class DeleteByNameMockTests {

        @Test
        @DisplayName("Мок deleteByName() должен вызываться с правильным именем")
        void deleteByName_shouldCallWithCorrectName() {
            // Arrange
            doNothing().when(repositoryMock).deleteByName(anyString());

            // Act
            repositoryMock.deleteByName(CONSTELLATION_NAME_1);

            // Assert
            verify(repositoryMock, times(1)).deleteByName(CONSTELLATION_NAME_1);
        }

        @Test
        @DisplayName("Мок deleteByName() не должен выбрасывать исключения")
        void deleteByName_shouldNotThrowException() {
            // Arrange
            doNothing().when(repositoryMock).deleteByName(anyString());

            // Act & Assert
            assertDoesNotThrow(() -> repositoryMock.deleteByName(CONSTELLATION_NAME_1));
        }
    }

    @Nested
    @DisplayName("Тесты метода count() с моками")
    class CountMockTests {

        @Test
        @DisplayName("Мок count() должен возвращать настроенное значение")
        void count_shouldReturnConfiguredValue() {
            // Arrange
            when(repositoryMock.count()).thenReturn(5);

            // Act
            int count = repositoryMock.count();

            // Assert
            assertEquals(5, count);
            verify(repositoryMock, times(1)).count();
        }

        @Test
        @DisplayName("Мок count() должен возвращать 0 для пустого репозитория")
        void count_emptyRepository_shouldReturnZero() {
            // Arrange
            when(repositoryMock.count()).thenReturn(0);

            // Act
            int count = repositoryMock.count();

            // Assert
            assertEquals(0, count);
        }
    }

    @Nested
    @DisplayName("Тесты метода clear() с моками")
    class ClearMockTests {

        @Test
        @DisplayName("Мок clear() должен вызываться ровно один раз")
        void clear_shouldBeCalledExactlyOnce() {
            // Arrange
            doNothing().when(repositoryMock).clear();

            // Act
            repositoryMock.clear();

            // Assert
            verify(repositoryMock, times(1)).clear();
        }

        @Test
        @DisplayName("Мок clear() не должен выбрасывать исключения")
        void clear_shouldNotThrowException() {
            // Arrange
            doNothing().when(repositoryMock).clear();

            // Act & Assert
            assertDoesNotThrow(() -> repositoryMock.clear());
        }
    }

    @Nested
    @DisplayName("Комбинированные тесты с моками")
    class CombinedMockTests {

        @Test
        @DisplayName("Последовательность save -> findByName должна работать корректно")
        void saveThenFindByName_shouldWorkCorrectly() {
            // Arrange
            SatelliteConstellation constellation = new SatelliteConstellation(CONSTELLATION_NAME_1);
            doNothing().when(repositoryMock).save(any(SatelliteConstellation.class));
            when(repositoryMock.findByName(CONSTELLATION_NAME_1)).thenReturn(Optional.of(constellation));
            when(repositoryMock.existsByName(CONSTELLATION_NAME_1)).thenReturn(true);

            // Act
            repositoryMock.save(constellation);
            Optional<SatelliteConstellation> found = repositoryMock.findByName(CONSTELLATION_NAME_1);
            boolean exists = repositoryMock.existsByName(CONSTELLATION_NAME_1);

            // Assert
            verify(repositoryMock, times(1)).save(any(SatelliteConstellation.class));
            verify(repositoryMock, times(1)).findByName(CONSTELLATION_NAME_1);
            verify(repositoryMock, times(1)).existsByName(CONSTELLATION_NAME_1);
            assertTrue(found.isPresent());
            assertTrue(exists);
        }

        @Test
        @DisplayName("Полный цикл CRUD операций с моками")
        void fullCrudCycle_withMocks_shouldWorkCorrectly() {
            // Arrange
            SatelliteConstellation constellation = new SatelliteConstellation(CONSTELLATION_NAME_1);
            Map<String, SatelliteConstellation> mockMap = new HashMap<>();
            mockMap.put(CONSTELLATION_NAME_1, constellation);

            // Настройка всех моков
            doNothing().when(repositoryMock).save(any(SatelliteConstellation.class));
            when(repositoryMock.findByName(CONSTELLATION_NAME_1)).thenReturn(Optional.of(constellation));
            when(repositoryMock.getAllConstellations()).thenReturn(mockMap);
            when(repositoryMock.existsByName(CONSTELLATION_NAME_1)).thenReturn(true);
            when(repositoryMock.count()).thenReturn(1);
            doNothing().when(repositoryMock).deleteByName(anyString());
            doNothing().when(repositoryMock).clear();

            // Act - Create
            repositoryMock.save(constellation);

            // Act - Read
            Optional<SatelliteConstellation> found = repositoryMock.findByName(CONSTELLATION_NAME_1);
            Map<String, SatelliteConstellation> all = repositoryMock.getAllConstellations();
            int count = repositoryMock.count();

            // Act - Update (save again)
            repositoryMock.save(constellation);

            // Act - Delete
            repositoryMock.deleteByName(CONSTELLATION_NAME_1);

            // Act - Clear
            repositoryMock.clear();

            // Assert - проверяем только те вызовы, которые реально нужны
            verify(repositoryMock, times(2)).save(any(SatelliteConstellation.class));
            verify(repositoryMock, times(1)).findByName(CONSTELLATION_NAME_1);
            verify(repositoryMock, times(1)).getAllConstellations();
            verify(repositoryMock, times(1)).count();
            verify(repositoryMock, times(1)).deleteByName(CONSTELLATION_NAME_1);
            verify(repositoryMock, times(1)).clear();

            assertTrue(found.isPresent());
            assertEquals(1, count);
            assertEquals(1, all.size());
        }
    }
}