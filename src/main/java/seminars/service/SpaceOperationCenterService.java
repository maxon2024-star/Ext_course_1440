package seminars.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import seminars.dto.AddSatelliteRequest;
import seminars.dto.MissionRequest;
import seminars.satellite.Satellite;
import seminars.annotation.LogExecutionTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpaceOperationCenterService {

    private final ConstellationService constellationService;
    private final SatelliteService satelliteService;

    // Применяем нашу будущую аннотацию-декоратор
    @LogExecutionTime
    public void addSatellite(AddSatelliteRequest request) {
        log.info("ФАСАД: Обработка запроса на добавление спутника в группировку {}", request.getConstellationName());

        // 1. Создаем группировку, если её еще нет
        if (!constellationService.constellationExists(request.getConstellationName())) {
            constellationService.createAndSaveConstellation(request.getConstellationName());
        }

        // 2. Создаем сам спутник через фабрику
        Satellite newSatellite = satelliteService.createSatellite(request.getSatelliteParam());

        // 3. Добавляем спутник в группировку
        constellationService.addSatelliteToConstellation(request.getConstellationName(), newSatellite);
    }

    @LogExecutionTime
    public void executeMission(MissionRequest request) {
        log.info("ФАСАД: Обработка запроса на выполнение миссии для группировки {}", request.getConstellationName());

        // Сначала активируем все спутники перед миссией (агрегация логики)
        constellationService.activateAllSatellites(request.getConstellationName());

        // Затем выполняем саму миссию
        constellationService.executeConstellationMission(request.getConstellationName());
    }

    // Дополнительный метод для Фасада: комплексная подготовка и вывод статуса
    @LogExecutionTime
    public void prepareAndReportSystem(String constellationName) {
        log.info("ФАСАД: Комплексная проверка и отчет по группировке {}", constellationName);
        constellationService.activateAllSatellites(constellationName);
        constellationService.showConstellationStatus(constellationName);
    }
}