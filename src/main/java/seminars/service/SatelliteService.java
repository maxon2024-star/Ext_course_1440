package seminars.service;

import seminars.satellite.Satellite;
import seminars.satellite.param.SatelliteParam;

public interface SatelliteService {
    Satellite createSatellite(SatelliteParam param);
}