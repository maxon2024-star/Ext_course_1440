import java.util.ArrayList;
import java.util.List;

public class SatelliteConstellation {
    private String constellationName;
    private List<Satellite> satellites;

    public SatelliteConstellation(String constellationName) {
        this.constellationName = constellationName;
        this.satellites = new ArrayList<>();
    }

    public void addSatellite(Satellite satellite) {
        satellites.add(satellite);
    }

    public void executeAllMissions() {
        for (Satellite satellite : satellites) {
            satellite.performMission();
        }
    }

    public List<Satellite> getSatellites() {
        return new ArrayList<>(satellites);
    }

    public String getConstellationName() {
        return constellationName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Группировка '").append(constellationName).append("' содержит спутники:\n");
        for (Satellite satellite : satellites) {
            sb.append("  ").append(satellite.toString()).append("\n");
        }
        return sb.toString();
    }
}