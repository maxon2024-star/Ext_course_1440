package telemetry.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import telemetry.dto.SatelliteEvent;
import telemetry.entity.InboxEvent;
import telemetry.repository.InboxRepository;
import telemetry.service.TelemetryGeneratorService;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class SatelliteEventListener {

    private final InboxRepository inboxRepository;
    private final TelemetryGeneratorService telemetryGeneratorService;
    private final ObjectMapper objectMapper;

    @Transactional // Атомарно: или обновим телеметрию и запишем в inbox, или произойдет откат
    @KafkaListener(topics = "satellite-events", groupId = "telemetry-group") // Топик должен совпадать с отправителем
    public void handleSatelliteEvent(String messagePayload) {
        try {
            SatelliteEvent event = objectMapper.readValue(messagePayload, SatelliteEvent.class);

            if (event.getEventId() == null) {
                log.warn("Событие пришло без eventId. Идемпотентность невозможна: {}", messagePayload);
                return;
            }

            // Проверка на идемпотентность (Паттерн Inbox)
            if (inboxRepository.existsById(event.getEventId())) {
                log.info("Событие {} уже было обработано. Пропускаем дубликат.", event.getEventId());
                return;
            }

            // Бизнес-логика обработки (используем правильные геттеры из DTO)
            if ("CREATED".equals(event.getEventType())) {
                telemetryGeneratorService.addSatellite(event.getSatelliteDetails());
            } else if ("DELETED".equals(event.getEventType())) {
                telemetryGeneratorService.removeSatellite(event.getSatelliteDetails());
            }

            // Запись в Inbox для предотвращения повторной обработки в будущем
            InboxEvent inboxEvent = new InboxEvent(
                    event.getEventId(),
                    event.getSatelliteDetails(),
                    event.getEventType(),
                    Instant.now()
            );
            inboxRepository.save(inboxEvent);
            log.info("Успешно обработано событие {}", event.getEventId());

        } catch (Exception e) {
            log.error("Ошибка при обработке сообщения из Kafka: {}", messagePayload, e);
            throw new RuntimeException(e); // Заставит Kafka повторить доставку сообщения
        }
    }
}