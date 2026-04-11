package seminars.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import seminars.satellite.CommunicationSatellite;
import seminars.satellite.ImagingSatellite;
import seminars.satellite.Satellite;
import seminars.satellite.param.CommunicationSatelliteParam;
import seminars.satellite.param.ImagingSatelliteParam;
import seminars.satellite.param.SatelliteParam;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SatelliteServiceTest {

    @Autowired
    private SatelliteService satelliteService;

    @Test
    void testCreateImagingSatelliteThroughService() {
        SatelliteParam param = new ImagingSatelliteParam("SpySat", 85.0, 4000.0);

        Satellite satellite = satelliteService.createSatellite(param);

        assertNotNull(satellite);
        assertTrue(satellite instanceof ImagingSatellite);
        assertEquals("SpySat", satellite.getName());
        assertEquals(85.0, satellite.getEnergy().getBatteryLevel());
        // Дополнительно можно проверить специфичные поля, если добавить геттеры в классы спутников
    }

    @Test
    void testCreateCommunicationSatelliteThroughService() {
        SatelliteParam param = new CommunicationSatelliteParam("RelaySat", 95.0, 1000.0);

        Satellite satellite = satelliteService.createSatellite(param);

        assertNotNull(satellite);
        assertTrue(satellite instanceof CommunicationSatellite);
        assertEquals("RelaySat", satellite.getName());
        assertEquals(95.0, satellite.getEnergy().getBatteryLevel());
    }
}