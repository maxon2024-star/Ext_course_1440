package seminars.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import seminars.constellation.SatelliteConstellation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class ConstellationRepository {
    private final Map<String, SatelliteConstellation> constellations = new HashMap<>();

    public void save(SatelliteConstellation constellation) {
        constellations.put(constellation.getConstellationName(), constellation);
        log.info("Сохранена группировка: {}", constellation.getConstellationName());
    }

    public Optional<SatelliteConstellation> findByName(String name) {
        return Optional.ofNullable(constellations.get(name));
    }

    public Map<String, SatelliteConstellation> getAllConstellations() {
        return new HashMap<>(constellations);
    }

    public boolean existsByName(String name) {
        return constellations.containsKey(name);
    }

    public void deleteByName(String name) {
        constellations.remove(name);
        log.info("Удалена группировка: {}", name);
    }

    public int count() {
        return constellations.size();
    }

    public void clear() {
        constellations.clear();
        log.info("Репозиторий очищен");
    }
}