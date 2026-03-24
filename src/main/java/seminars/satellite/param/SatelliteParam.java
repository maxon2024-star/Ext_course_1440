package seminars.satellite.param;

import lombok.AllArgsConstructor;
import lombok.Getter;
import seminars.satellite.SatelliteType;

@Getter
@AllArgsConstructor
public abstract class SatelliteParam {
    protected SatelliteType type;
    protected String name;
    protected double batteryLevel;
}