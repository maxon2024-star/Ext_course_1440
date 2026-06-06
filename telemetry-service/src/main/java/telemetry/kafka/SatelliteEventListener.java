package telemetry.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

// ИСПРАВЛЕННЫЙ ИМПОРТ:
import telemetry.dto.SatelliteEvent;

@Slf4j
@Service
public class SatelliteEventListener {

    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 2000, multiplier = 2.0),
            autoCreateTopics = "true",
            dltTopicSuffix = ".dlt"
    )
    @KafkaListener(topics = "satellite-events", groupId = "telemetry-group")
    public void handleSatelliteEvent(@Payload SatelliteEvent event,
                                     @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("Получено событие из топика [{}]: {}", topic, event);

        if ("ERROR".equals(event.getConstellationName())) {
            throw new RuntimeException("Ошибка обработки события! Отправляем на повтор, а затем в DLQ.");
        }

        if ("CREATED".equals(event.getEventType())) {
            log.info("Телеметрия: Регистрируем новый спутник в системе наблюдения...");
        } else if ("DELETED".equals(event.getEventType())) {
            log.info("Телеметрия: Удаляем спутник из системы наблюдения...");
        }
    }

    @KafkaListener(topics = "satellite-events.dlt", groupId = "telemetry-dlq-group")
    public void handleDlq(@Payload SatelliteEvent event,
                          @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.error("ВНИМАНИЕ! Сообщение попало в DLQ (Мертвая очередь) топик [{}]. " +
                "Событие: {}. Требуется ручное вмешательство или логирование.", topic, event);
    }
}