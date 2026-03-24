package seminars.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import seminars.constellation.SatelliteConstellation;
import seminars.repository.ConstellationRepository;
import seminars.satellite.CommunicationSatellite;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("SpaceOperationCenterService - Мок тесты")
@ExtendWith(MockitoExtension.class)
class ConstellationServiceMockTest {

    private static final String CONSTELLATION_NAME = "Орбита-Тест";
    private static final String SATELLITE_NAME = "Спутник-Тест";
    private static final double ENERGY_LEVEL = 0.90;
    private static final double BANDWIDTH = 500.0;

    @Mock
    private ConstellationRepository repositoryMock;

    @InjectMocks
    private ConstellationService service;

    @Test
    @DisplayName("createAndSaveConstellation должен вызывать save на репозитории")
    void createAndSaveConstellation_shouldCallRepositorySave() {
        // Arrange
        doNothing().when(repositoryMock).save(any(SatelliteConstellation.class));

        // Act
        SatelliteConstellation result = service.createAndSaveConstellation(CONSTELLATION_NAME);

        // Assert
        assertNotNull(result);
        assertEquals(CONSTELLATION_NAME, result.getConstellationName());
        verify(repositoryMock, times(1)).save(any(SatelliteConstellation.class));
    }

    @Test
    @DisplayName("addSatelliteToConstellation с существующей группировкой должен добавлять спутник")
    void addSatelliteToConstellation_existingConstellation_shouldAddSatellite() {
        // Arrange
        SatelliteConstellation constellation = SatelliteConstellation.builder(CONSTELLATION_NAME).build();
        CommunicationSatellite satellite = new CommunicationSatellite(SATELLITE_NAME, ENERGY_LEVEL, BANDWIDTH);
        when(repositoryMock.findByName(CONSTELLATION_NAME)).thenReturn(Optional.of(constellation));

        // Act
        service.addSatelliteToConstellation(CONSTELLATION_NAME, satellite);

        // Assert
        assertEquals(1, constellation.getSatellites().size());
        verify(repositoryMock, times(1)).findByName(CONSTELLATION_NAME);
    }

    @Test
    @DisplayName("addSatelliteToConstellation с несуществующей группировкой не должен выбрасывать исключения")
    void addSatelliteToConstellation_nonExistentConstellation_shouldNotThrow() {
        // Arrange
        CommunicationSatellite satellite = new CommunicationSatellite(SATELLITE_NAME, ENERGY_LEVEL, BANDWIDTH);
        when(repositoryMock.findByName(CONSTELLATION_NAME)).thenReturn(Optional.empty());

        // Act & Assert
        assertDoesNotThrow(() -> service.addSatelliteToConstellation(CONSTELLATION_NAME, satellite));
        verify(repositoryMock, times(1)).findByName(CONSTELLATION_NAME);
    }

    @Test
    @DisplayName("activateAllSatellites должен активировать все спутники в группировке")
    void activateAllSatellites_shouldActivateAllSatellites() {
        // Arrange
        SatelliteConstellation constellation = SatelliteConstellation.builder(CONSTELLATION_NAME).build();
        CommunicationSatellite satellite = new CommunicationSatellite(SATELLITE_NAME, ENERGY_LEVEL, BANDWIDTH);
        constellation.addSatellite(satellite);
        when(repositoryMock.findByName(CONSTELLATION_NAME)).thenReturn(Optional.of(constellation));

        // Act
        service.activateAllSatellites(CONSTELLATION_NAME);

        // Assert
        assertTrue(satellite.getState().isActive());
        verify(repositoryMock, times(1)).findByName(CONSTELLATION_NAME);
    }

    @Test
    @DisplayName("executeConstellationMission должен выполнять миссии всех спутников")
    void executeConstellationMission_shouldExecuteAllMissions() {
        // Arrange
        SatelliteConstellation constellation = SatelliteConstellation.builder(CONSTELLATION_NAME).build();
        CommunicationSatellite satellite = new CommunicationSatellite(SATELLITE_NAME, ENERGY_LEVEL, BANDWIDTH);
        satellite.activate();
        constellation.addSatellite(satellite);
        when(repositoryMock.findByName(CONSTELLATION_NAME)).thenReturn(Optional.of(constellation));

        // Act
        service.executeConstellationMission(CONSTELLATION_NAME);

        // Assert
        assertTrue(satellite.getEnergy().getBatteryLevel() < ENERGY_LEVEL);
        verify(repositoryMock, times(1)).findByName(CONSTELLATION_NAME);
    }

    @Test
    @DisplayName("constellationExists должен возвращать результат из репозитория")
    void constellationExists_shouldReturnRepositoryResult() {
        // Arrange
        when(repositoryMock.existsByName(CONSTELLATION_NAME)).thenReturn(true);

        // Act
        boolean exists = service.constellationExists(CONSTELLATION_NAME);

        // Assert
        assertTrue(exists);
        verify(repositoryMock, times(1)).existsByName(CONSTELLATION_NAME);
    }

    @Test
    @DisplayName("getConstellationCount должен возвращать количество из репозитория")
    void getConstellationCount_shouldReturnRepositoryCount() {
        // Arrange
        when(repositoryMock.count()).thenReturn(5);

        // Act
        int count = service.getConstellationCount();

        // Assert
        assertEquals(5, count);
        verify(repositoryMock, times(1)).count();
    }
}