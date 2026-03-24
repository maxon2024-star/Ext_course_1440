package seminars.satellite.factory;

import seminars.satellite.ImagingSatellite;
import seminars.satellite.Satellite;

public class ImagingSatelliteFactory implements SatelliteFactory {
    @Override
    public Satellite createSatellite(String name, double initialEnergy, double resolution) {
        return new ImagingSatellite(name, initialEnergy, resolution);
    }
}