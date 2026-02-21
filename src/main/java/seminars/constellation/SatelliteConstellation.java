package seminars.constellation;

import lombok.Getter;
import lombok.ToString;
import seminars.satellite.Satellite;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class SatelliteConstellation {
    private final String constellationName;
    private final List<Satellite> satellites;

    public SatelliteConstellation(String constellationName) {
        this.constellationName = constellationName;
        this.satellites = new ArrayList<>();
    }

    public void addSatellite(Satellite satellite) {
        satellites.add(satellite);
    }
}