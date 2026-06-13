package seminars.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import seminars.entity.OutboxEvent;
import seminars.repository.OutboxRepository;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxScheduler {

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelayString = "${outbox.scheduler.delay:5000}")
    @Transactional
    public void processOutboxEvents() {
        List<OutboxEvent> pendingEvents = outboxRepository.findByStatusOrderByCreatedAtAsc(OutboxEvent.OutboxStatus.PENDING);

        for (OutboxEvent event : pendingEvents) {
            try {
                // Топик изменен на "satellite-events" + добавлен таймаут
                kafkaTemplate.send("satellite-events", event.getAggregateId(), event.getPayload())
                        .get(3, TimeUnit.SECONDS);

                event.setStatus(OutboxEvent.OutboxStatus.SENT);
                outboxRepository.save(event);
                log.info("Successfully sent outbox event: {}", event.getId());
            } catch (Exception e) {
                log.error("Failed to send outbox event to Kafka: {}. Will retry.", event.getId(), e);
            }
        }
    }
}