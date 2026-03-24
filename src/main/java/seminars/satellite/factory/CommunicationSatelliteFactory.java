package seminars.satellite.factory;

import seminars.satellite.CommunicationSatellite;
import seminars.satellite.Satellite;

public class CommunicationSatelliteFactory implements SatelliteFactory {
    @Override
    public Satellite createSatellite(String name, double initialEnergy, double bandwidth) {
        return new CommunicationSatellite(name, initialEnergy, bandwidth);
    }
}