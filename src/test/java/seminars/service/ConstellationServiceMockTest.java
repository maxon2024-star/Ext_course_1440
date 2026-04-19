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
        when(repositoryMock.save(any(SatelliteConstellation.class))).thenAnswer(i -> i.getArgument(0));
        SatelliteConstellation result = service.createAndSaveConstellation(CONSTELLATION_NAME);
        assertNotNull(result);
        assertEquals(CONSTELLATION_NAME, result.getConstellationName());
        verify(repositoryMock, times(1)).save(any(SatelliteConstellation.class));
    }

    @Test
    @DisplayName("addSatelliteToConstellation с существующей группировкой должен добавлять спутник")
    void addSatelliteToConstellation_existingConstellation_shouldAddSatellite() {
        SatelliteConstellation constellation = SatelliteConstellation.builder(CONSTELLATION_NAME).build();
        CommunicationSatellite satellite = new CommunicationSatellite(SATELLITE_NAME, ENERGY_LEVEL, BANDWIDTH);
        when(repositoryMock.findByName(CONSTELLATION_NAME)).thenReturn(Optional.of(constellation));
        service.addSatelliteToConstellation(CONSTELLATION_NAME, satellite);
        assertEquals(1, constellation.getSatellites().size());
        verify(repositoryMock, times(1)).findByName(CONSTELLATION_NAME);
    }

    @Test
    @DisplayName("addSatelliteToConstellation с несуществующей группировкой не должен выбрасывать исключения")
    void addSatelliteToConstellation_nonExistentConstellation_shouldNotThrow() {
        CommunicationSatellite satellite = new CommunicationSatellite(SATELLITE_NAME, ENERGY_LEVEL, BANDWIDTH);
        when(repositoryMock.findByName(CONSTELLATION_NAME)).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> service.addSatelliteToConstellation(CONSTELLATION_NAME, satellite));
        verify(repositoryMock, times(1)).findByName(CONSTELLATION_NAME);
    }

    @Test
    @DisplayName("activateAllSatellites должен активировать все спутники в группировке")
    void activateAllSatellites_shouldActivateAllSatellites() {
        SatelliteConstellation constellation = SatelliteConstellation.builder(CONSTELLATION_NAME).build();
        CommunicationSatellite satellite = new CommunicationSatellite(SATELLITE_NAME, ENERGY_LEVEL, BANDWIDTH);
        constellation.addSatellite(satellite);
        when(repositoryMock.findByName(CONSTELLATION_NAME)).thenReturn(Optional.of(constellation));
        service.activateAllSatellites(CONSTELLATION_NAME);
        assertTrue(satellite.getState().isActive());
        verify(repositoryMock, times(1)).findByName(CONSTELLATION_NAME);
    }

    @Test
    @DisplayName("executeConstellationMission должен выполнять миссии всех спутников")
    void executeConstellationMission_shouldExecuteAllMissions() {
        SatelliteConstellation constellation = SatelliteConstellation.builder(CONSTELLATION_NAME).build();
        CommunicationSatellite satellite = new CommunicationSatellite(SATELLITE_NAME, ENERGY_LEVEL, BANDWIDTH);
        satellite.activate();
        constellation.addSatellite(satellite);
        when(repositoryMock.findByName(CONSTELLATION_NAME)).thenReturn(Optional.of(constellation));
        service.executeConstellationMission(CONSTELLATION_NAME);
        assertTrue(satellite.getEnergy().getBatteryLevel() < ENERGY_LEVEL);
        verify(repositoryMock, times(1)).findByName(CONSTELLATION_NAME);
    }

    @Test
    @DisplayName("constellationExists должен возвращать результат из репозитория")
    void constellationExists_shouldReturnRepositoryResult() {
        when(repositoryMock.existsByName(CONSTELLATION_NAME)).thenReturn(true);
        boolean exists = service.constellationExists(CONSTELLATION_NAME);
        assertTrue(exists);
        verify(repositoryMock, times(1)).existsByName(CONSTELLATION_NAME);
    }

    @Test
    @DisplayName("getConstellationCount должен возвращать количество из репозитория")
    void getConstellationCount_shouldReturnRepositoryCount() {
        // ИСПРАВЛЕНИЕ: передаем 5L вместо 5
        when(repositoryMock.count()).thenReturn(5L);
        int count = service.getConstellationCount();
        assertEquals(5, count);
        verify(repositoryMock, times(1)).count();
    }
}