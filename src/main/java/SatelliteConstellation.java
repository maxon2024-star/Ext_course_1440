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
        System.out.printf("ВЫПОЛНЕНИЕ МИССИЙ ГРУППИРОВКИ %s%n", constellationName.toUpperCase());
        System.out.println("==================================================");
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
        sb.append("SatelliteConstellation{constellationName='").append(constellationName).append("', satellites=[");
        for (int i = 0; i < satellites.size(); i++) {
            sb.append(satellites.get(i));
            if (i < satellites.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]}");
        return sb.toString();
    }
}