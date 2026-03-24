package seminars.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import seminars.repository.ConstellationRepository;
import seminars.satellite.Satellite;
import seminars.constellation.SatelliteConstellation;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpaceOperationCenterService {
    private final ConstellationRepository constellationRepository;

    public SatelliteConstellation createAndSaveConstellation(String name) {
        // Изменение: используем Builder вместо конструктора
        SatelliteConstellation constellation = SatelliteConstellation.builder(name).build();
        constellationRepository.save(constellation);
        log.info("Создана спутниковая группировка: {}", name);
        return constellation;
    }

    public void addSatelliteToConstellation(String constellationName, Satellite satellite) {
        constellationRepository.findByName(constellationName).ifPresentOrElse(
                constellation -> {
                    constellation.addSatellite(satellite);
                    log.info("Добавлен спутник {} в группировку {}", satellite.getName(), constellationName);
                },
                () -> log.error("Группировка '{}' не найдена", constellationName)
        );
    }

    public void executeConstellationMission(String constellationName) {
        log.info("=== ВЫПОЛНЕНИЕ МИССИЙ ДЛЯ ГРУППИРОВКИ: {} ===", constellationName);
        constellationRepository.findByName(constellationName).ifPresentOrElse(
                constellation -> {
                    log.info("ВЫПОЛНЕНИЕ МИССИЙ ГРУППИРОВКИ {}", constellationName.toUpperCase());
                    log.info("==================================================");
                    for (Satellite satellite : constellation.getSatellites()) {
                        satellite.executeMission();
                    }
                },
                () -> log.error("Группировка '{}' не найдена", constellationName)
        );
    }

    public void activateAllSatellites(String constellationName) {
        log.info("=== АКТИВАЦИЯ СПУТНИКОВ В ГРУППИРОВКЕ: {} ===", constellationName);
        constellationRepository.findByName(constellationName).ifPresentOrElse(
                constellation -> {
                    for (Satellite satellite : constellation.getSatellites()) {
                        satellite.activate();
                    }
                },
                () -> log.error("Группировка '{}' не найдена", constellationName)
        );
    }

    public void showConstellationStatus(String constellationName) {
        log.info("=== СТАТУС ГРУППИРОВКИ: {} ===", constellationName);
        constellationRepository.findByName(constellationName).ifPresentOrElse(
                constellation -> {
                    log.info("Количество спутников: {}", constellation.getSatellites().size());
                    for (Satellite satellite : constellation.getSatellites()) {
                        log.info("{}", satellite.getState());
                    }
                },
                () -> log.error("Группировка '{}' не найдена", constellationName)
        );
    }

    public boolean constellationExists(String constellationName) {
        return constellationRepository.existsByName(constellationName);
    }

    public int getConstellationCount() {
        return constellationRepository.count();
    }
}