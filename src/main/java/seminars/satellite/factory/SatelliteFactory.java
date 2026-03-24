package seminars.satellite.factory;

import seminars.satellite.Satellite;

public interface SatelliteFactory {
    // specificParam: в зависимости от реализации это будет либо bandwidth, либо resolution
    Satellite createSatellite(String name, double initialEnergy, double specificParam);
}