package seminars.satellite.factory;

import seminars.satellite.Satellite;
import seminars.satellite.SatelliteType;
import seminars.satellite.param.SatelliteParam;

public interface SatelliteFactory {
    Satellite createSatelliteWithParameter(SatelliteParam param);
    boolean isSatelliteTypeSupported(SatelliteType type);
}