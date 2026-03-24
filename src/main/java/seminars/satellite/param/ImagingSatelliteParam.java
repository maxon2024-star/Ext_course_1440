package seminars.satellite.param;

import lombok.Getter;
import seminars.satellite.SatelliteType;

@Getter
public class ImagingSatelliteParam extends SatelliteParam {
    private final double resolution;

    public ImagingSatelliteParam(String name, double batteryLevel, double resolution) {
        super(SatelliteType.IMAGE, name, batteryLevel);
        this.resolution = resolution;
    }
}