package seminars.satellites.factory;

import org.junit.jupiter.api.Test;
import seminars.exception.SpaceOperationException;
import seminars.satellite.CommunicationSatellite;
import seminars.satellite.ImagingSatellite;
import seminars.satellite.Satellite;
import seminars.satellite.factory.CommunicationSatelliteFactory;
import seminars.satellite.factory.ImagingSatelliteFactory;
import seminars.satellite.factory.SatelliteFactory;
import seminars.satellite.param.CommunicationSatelliteParam;
import seminars.satellite.param.ImagingSatelliteParam;
import seminars.satellite.param.SatelliteParam;

import static org.junit.jupiter.api.Assertions.*;

class SatelliteFactoryTest {

    @Test
    void testCommunicationSatelliteFactoryCreatesCorrectSatellite() {
        SatelliteFactory factory = new CommunicationSatelliteFactory();
        SatelliteParam param = new CommunicationSatelliteParam("ComSat-1", 100.0, 500.0);

        Satellite satellite = factory.createSatelliteWithParameter(param);

        assertTrue(satellite instanceof CommunicationSatellite);
        assertEquals("ComSat-1", satellite.getName());
        assertEquals(100.0, satellite.getEnergy().getBatteryLevel());
    }

    @Test
    void testImagingSatelliteFactoryCreatesCorrectSatellite() {
        SatelliteFactory factory = new ImagingSatelliteFactory();
        SatelliteParam param = new ImagingSatelliteParam("ImgSat-1", 90.0, 1080.0);

        Satellite satellite = factory.createSatelliteWithParameter(param);

        assertTrue(satellite instanceof ImagingSatellite);
        assertEquals("ImgSat-1", satellite.getName());
        assertEquals(90.0, satellite.getEnergy().getBatteryLevel());
    }

    @Test
    void testFactoryThrowsExceptionOnWrongParamType() {
        SatelliteFactory factory = new CommunicationSatelliteFactory();
        SatelliteParam wrongParam = new ImagingSatelliteParam("Wrong-1", 100.0, 1080.0);

        assertThrows(SpaceOperationException.class, () -> factory.createSatelliteWithParameter(wrongParam));
    }
}