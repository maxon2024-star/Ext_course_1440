import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SpaceOperationCenterService {
    private final ConstellationRepository constellationRepository;

    public SpaceOperationCenterService(ConstellationRepository constellationRepository) {
        this.constellationRepository = constellationRepository;
    }

    public SatelliteConstellation createAndSaveConstellation(String name) {
        SatelliteConstellation constellation = new SatelliteConstellation(name);
        constellationRepository.save(constellation);
        System.out.printf("Сохранена группировка: %s%n", name);
        return constellation;
    }

    public void addSatelliteToConstellation(String constellationName, Satellite satellite) {
        SatelliteConstellation constellation = constellationRepository.findById(constellationName);
        if (constellation != null) {
            constellation.addSatellite(satellite);
            System.out.printf("Добавлен спутник %s в группировку %s%n",
                    satellite.getName(), constellationName);
        } else {
            System.out.printf("Группировка %s не найдена%n", constellationName);
        }
    }

    public void executeConstellationMission(String constellationName) {
        SatelliteConstellation constellation = constellationRepository.findById(constellationName);
        if (constellation != null) {
            System.out.printf("=== ВЫПОЛНЕНИЕ МИССИЙ ДЛЯ ГРУППИРОВКИ: %s ===%n", constellationName);
            constellation.executeAllMissions();
        } else {
            System.out.printf("Группировка %s не найдена%n", constellationName);
        }
    }

    public void activateAllSatellites(String constellationName) {
        SatelliteConstellation constellation = constellationRepository.findById(constellationName);
        if (constellation != null) {
            System.out.printf("=== АКТИВАЦИЯ СПУТНИКОВ В ГРУППИРОВКЕ: %s ===%n", constellationName);
            for (Satellite satellite : constellation.getSatellites()) {
                boolean success = satellite.activate();
                if (success) {
                    System.out.printf("✅ %s: Активация успешна%n", satellite.getName());
                } else {
                    System.out.printf("🛑 %s: Ошибка активации (заряд: %.0f%%)%n",
                            satellite.getName(), satellite.getBatteryLevel() * 100);
                }
            }
        } else {
            System.out.printf("Группировка %s не найдена%n", constellationName);
        }
    }

    public void showConstellationStatus(String constellationName) {
        SatelliteConstellation constellation = constellationRepository.findById(constellationName);
        if (constellation != null) {
            System.out.printf("=== СТАТУС ГРУППИРОВКИ: %s ===%n", constellationName);
            System.out.printf("Количество спутников: %d%n", constellation.getSatellites().size());
            for (Satellite satellite : constellation.getSatellites()) {
                System.out.printf("  %s%n", satellite.toString());
            }
        } else {
            System.out.printf("Группировка %s не найдена%n", constellationName);
        }
    }

    public void displayAllConstellations() {
        // Используем импортированный Map
        Map<String, SatelliteConstellation> all = constellationRepository.getAllConstellations();
        System.out.println("\n=== ВСЕ ГРУППИРОВКИ В РЕПОЗИТОРИИ ===");
        for (Map.Entry<String, SatelliteConstellation> entry : all.entrySet()) {
            System.out.printf("%s = %s%n", entry.getKey(), entry.getValue());
        }
    }
}