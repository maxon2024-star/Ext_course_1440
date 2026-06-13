package seminars.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seminars.dto.AddSatelliteRequest;
import seminars.dto.MissionRequest;
import seminars.dto.SatelliteEvent;
import seminars.entity.OutboxEvent;
import seminars.repository.OutboxRepository;
import seminars.satellite.Satellite;
import seminars.annotation.LogExecutionTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpaceOperationCenterService {

    private final ConstellationService constellationService;
    private final SatelliteService satelliteService;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @LogExecutionTime
    @Transactional // Гарантируем атомарность БД: сохранится и группировка, и Outbox
    public void addSatellite(AddSatelliteRequest request) {
        log.info("ФАСАД: Обработка запроса на добавление спутника в группировку {}", request.getConstellationName());

        if (!constellationService.constellationExists(request.getConstellationName())) {
            constellationService.createAndSaveConstellation(request.getConstellationName());
        }

        Satellite newSatellite = satelliteService.createSatellite(request.getSatelliteParam());
        constellationService.addSatelliteToConstellation(request.getConstellationName(), newSatellite);

        // Паттерн Outbox: вместо прямой отправки в Kafka, формируем событие и пишем в БД
        SatelliteEvent event = new SatelliteEvent();
        event.setEventType("CREATED");
        event.setConstellationName(request.getConstellationName());
        event.setSatelliteDetails(newSatellite.toString());

        saveToOutbox(event, newSatellite.toString());
    }

    @LogExecutionTime
    @Transactional
    public void deleteSatellite(String constellationName, String satelliteId) {
        log.info("ФАСАД: Обработка запроса на удаление спутника {} из {}", satelliteId, constellationName);

        // Логика удаления спутника из группировки (если она реализована в constellationService)
        // constellationService.removeSatellite(...)

        // Паттерн Outbox: событие удаления
        SatelliteEvent event = new SatelliteEvent();
        event.setEventType("DELETED");
        event.setConstellationName(constellationName);
        event.setSatelliteDetails(satelliteId);

        saveToOutbox(event, satelliteId);
    }

    // Вспомогательный метод для сохранения в таблицу outbox
    private void saveToOutbox(SatelliteEvent event, String aggregateId) {
        try {
            OutboxEvent outboxEvent = new OutboxEvent();
            outboxEvent.setId(event.getEventId());
            outboxEvent.setAggregateId(aggregateId);
            outboxEvent.setEventType(event.getEventType());
            outboxEvent.setPayload(objectMapper.writeValueAsString(event));
            outboxEvent.setStatus(OutboxEvent.OutboxStatus.PENDING);

            outboxRepository.save(outboxEvent);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при сериализации события для Outbox", e);
        }
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