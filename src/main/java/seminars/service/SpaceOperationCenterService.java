package seminars.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import seminars.dto.AddSatelliteRequest;
import seminars.dto.MissionRequest;
import seminars.dto.SatelliteEvent;
import seminars.kafka.SatelliteEventProducer;
import seminars.satellite.Satellite;
import seminars.annotation.LogExecutionTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpaceOperationCenterService {

    private final ConstellationService constellationService;
    private final SatelliteService satelliteService;
    private final SatelliteEventProducer eventProducer; // ДОБАВЛЕНО

    @LogExecutionTime
    public void addSatellite(AddSatelliteRequest request) {
        log.info("ФАСАД: Обработка запроса на добавление спутника в группировку {}", request.getConstellationName());

        if (!constellationService.constellationExists(request.getConstellationName())) {
            constellationService.createAndSaveConstellation(request.getConstellationName());
        }

        Satellite newSatellite = satelliteService.createSatellite(request.getSatelliteParam());
        constellationService.addSatelliteToConstellation(request.getConstellationName(), newSatellite);

        // ДОБАВЛЕНО: Уведомляем другие микросервисы через Kafka
        SatelliteEvent event = new SatelliteEvent(
                "CREATED",
                request.getConstellationName(),
                newSatellite.toString() // Или newSatellite.getId() если есть
        );
        eventProducer.sendEvent(event);
    }

    // Пример метода удаления спутника (если он есть в системе)
    @LogExecutionTime
    public void deleteSatellite(String constellationName, String satelliteId) {
        log.info("ФАСАД: Обработка запроса на удаление спутника {} из {}", satelliteId, constellationName);

        // Тут ваша логика удаления: constellationService.removeSatellite(...)

        // Отправка события об удалении
        SatelliteEvent event = new SatelliteEvent("DELETED", constellationName, satelliteId);
        eventProducer.sendEvent(event);
    }

    @LogExecutionTime
    public void executeMission(MissionRequest request) {
        log.info("ФАСАД: Обработка запроса на выполнение миссии для группировки {}", request.getConstellationName());
        constellationService.activateAllSatellites(request.getConstellationName());
        constellationService.executeConstellationMission(request.getConstellationName());
    }

    @LogExecutionTime
    public void prepareAndReportSystem(String constellationName) {
        log.info("ФАСАД: Комплексная проверка и отчет по группировке {}", constellationName);
        constellationService.activateAllSatellites(constellationName);
        constellationService.showConstellationStatus(constellationName);
    }
}