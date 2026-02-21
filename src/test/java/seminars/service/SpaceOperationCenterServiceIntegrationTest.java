package seminars.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import seminars.repository.ConstellationRepository;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SpaceOperationCenterService - Интеграционные тесты")
@SpringBootTest
class SpaceOperationCenterServiceIntegrationTest {

    private static final String CONSTELLATION_NAME = "Орбита-Сервис-Тест";

    @Autowired
    private SpaceOperationCenterService service;

    @Autowired
    private ConstellationRepository repository;

    @BeforeEach
    void setUp() {
        repository.clear();
    }

    @Test
    @DisplayName("Создание группировки через сервис должно сохранять в репозиторий")
    void createConstellation_shouldSaveToRepository() {
        // Act
        service.createAndSaveConstellation(CONSTELLATION_NAME);

        // Assert
        assertTrue(repository.existsByName(CONSTELLATION_NAME));
        assertEquals(1, repository.count());
    }

    @Test
    @DisplayName("getConstellationCount должен возвращать актуальное количество")
    void getConstellationCount_shouldReturnActualCount() {
        // Arrange
        service.createAndSaveConstellation(CONSTELLATION_NAME);
        service.createAndSaveConstellation("Орбита-2");

        // Assert
        assertEquals(2, service.getConstellationCount());
    }

    @Test
    @DisplayName("constellationExists должен проверять существование в репозитории")
    void constellationExists_shouldCheckRepository() {
        // Arrange
        service.createAndSaveConstellation(CONSTELLATION_NAME);

        // Assert
        assertTrue(service.constellationExists(CONSTELLATION_NAME));
        assertFalse(service.constellationExists("Несуществующая"));
    }
}