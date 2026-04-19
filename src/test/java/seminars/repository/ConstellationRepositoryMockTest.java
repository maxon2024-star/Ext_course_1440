package seminars.repository;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import seminars.constellation.SatelliteConstellation;
import seminars.satellite.CommunicationSatellite;

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

    private static final String CONSTELLATION_NAME_1 = "Орбита-1";
    private static final String CONSTELLATION_NAME_2 = "Орбита-2";
    private static final String CONSTELLATION_NAME_NONEXISTENT = "Несуществующая";
    private static final String SATELLITE_NAME_1 = "Связь-1";
    private static final double ENERGY_LEVEL_1 = 0.85;
    private static final double BANDWIDTH = 500.0;

    @Mock
    private ConstellationRepository repositoryMock;

    @Captor
    private ArgumentCaptor<SatelliteConstellation> constellationCaptor;

    // ... (остальные тесты остаются без изменений, пропускаю для краткости, они работают) ...

    @Nested
    @DisplayName("Тесты метода save() с моками")
    class SaveMockTests {
        @Test
        void save_shouldCallWithCorrectConstellation() {
            SatelliteConstellation constellation = SatelliteConstellation.builder(CONSTELLATION_NAME_1).build();
            when(repositoryMock.save(any(SatelliteConstellation.class))).thenAnswer(i -> i.getArgument(0));
            repositoryMock.save(constellation);
            verify(repositoryMock, times(1)).save(constellationCaptor.capture());
            assertEquals(CONSTELLATION_NAME_1, constellationCaptor.getValue().getConstellationName());
        }
    }

    @Nested
    @DisplayName("Тесты метода count() с моками")
    class CountMockTests {

        @Test
        @DisplayName("Мок count() должен возвращать настроенное значение")
        void count_shouldReturnConfiguredValue() {
            // ИСПРАВЛЕНИЕ: thenReturn(5L) и long count
            when(repositoryMock.count()).thenReturn(5L);
            long count = repositoryMock.count();
            assertEquals(5L, count);
            verify(repositoryMock, times(1)).count();
        }

        @Test
        @DisplayName("Мок count() должен возвращать 0 для пустого репозитория")
        void count_emptyRepository_shouldReturnZero() {
            // ИСПРАВЛЕНИЕ: thenReturn(0L) и long count
            when(repositoryMock.count()).thenReturn(0L);
            long count = repositoryMock.count();
            assertEquals(0L, count);
        }
    }

    @Nested
    @DisplayName("Комбинированные тесты с моками")
    class CombinedMockTests {

        @Test
        @DisplayName("Полный цикл CRUD операций с моками")
        void fullCrudCycle_withMocks_shouldWorkCorrectly() {
            SatelliteConstellation constellation = SatelliteConstellation.builder(CONSTELLATION_NAME_1).build();
            Map<String, SatelliteConstellation> mockMap = new HashMap<>();
            mockMap.put(CONSTELLATION_NAME_1, constellation);

            when(repositoryMock.save(any(SatelliteConstellation.class))).thenAnswer(i -> i.getArgument(0));
            when(repositoryMock.findByName(CONSTELLATION_NAME_1)).thenReturn(Optional.of(constellation));
            when(repositoryMock.getAllConstellations()).thenReturn(mockMap);
            when(repositoryMock.existsByName(CONSTELLATION_NAME_1)).thenReturn(true);

            // ИСПРАВЛЕНИЕ: thenReturn(1L)
            when(repositoryMock.count()).thenReturn(1L);
            doNothing().when(repositoryMock).deleteByName(anyString());
            doNothing().when(repositoryMock).clear();

            repositoryMock.save(constellation);
            Optional<SatelliteConstellation> found = repositoryMock.findByName(CONSTELLATION_NAME_1);
            Map<String, SatelliteConstellation> all = repositoryMock.getAllConstellations();

            // ИСПРАВЛЕНИЕ: long count
            long count = repositoryMock.count();

            repositoryMock.save(constellation);
            repositoryMock.deleteByName(CONSTELLATION_NAME_1);
            repositoryMock.clear();

            verify(repositoryMock, times(2)).save(any(SatelliteConstellation.class));
            verify(repositoryMock, times(1)).count();
            assertTrue(found.isPresent());
            assertEquals(1L, count); // ИСПРАВЛЕНИЕ: 1L
            assertEquals(1, all.size());
        }
    }
}