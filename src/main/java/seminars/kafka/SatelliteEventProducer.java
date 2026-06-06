package seminars.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import seminars.dto.SatelliteEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class SatelliteEventProducer {

    private final KafkaTemplate<String, SatelliteEvent> kafkaTemplate;
    private static final String TOPIC = "satellite-events";

    public void sendEvent(SatelliteEvent event) {
        log.info("Отправка события в Kafka топик {}: {}", TOPIC, event);
        // В качестве ключа передаем тип события, чтобы похожие события попадали в одну партицию
        kafkaTemplate.send(TOPIC, event.getEventType(), event);
    }
}