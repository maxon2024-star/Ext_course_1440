package seminars.dto;

import lombok.Data;
import seminars.satellite.param.SatelliteParam;

@Data
public class AddSatelliteRequest {
    private String constellationName;
    private SatelliteParam satelliteParam;
}