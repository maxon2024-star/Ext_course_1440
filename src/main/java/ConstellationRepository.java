import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ConstellationRepository {
    private final Map<String, SatelliteConstellation> constellations = new HashMap<>();

    public void save(SatelliteConstellation constellation) {
        constellations.put(constellation.getConstellationName(), constellation);
    }

    public SatelliteConstellation findById(String name) {
        return constellations.get(name);
    }

    public boolean exists(String name) {
        return constellations.containsKey(name);
    }

    public void delete(String name) {
        constellations.remove(name);
    }

    public Map<String, SatelliteConstellation> getAllConstellations() {
        return new HashMap<>(constellations);
    }

    public int getCount() {
        return constellations.size();
    }
}