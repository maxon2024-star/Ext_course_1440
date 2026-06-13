package seminars.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import seminars.entity.OutboxEvent;
import seminars.repository.OutboxRepository;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class OutboxIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.6.1"));

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Autowired
    private SatelliteService satelliteService;

    @Autowired
    private OutboxRepository outboxRepository;

    @Test
    void shouldAtomicallySaveSatelliteAndOutboxEvent() {
        // Arrange
        AddSatelliteRequest request = new AddSatelliteRequest(/* параметры */);

        // Act
        satelliteService.addSatellite(request);

        // Assert
        List<OutboxEvent> outboxEvents = outboxRepository.findAll();
        assertThat(outboxEvents).hasSize(1);
        assertThat(outboxEvents.get(0).getStatus()).isEqualTo(OutboxEvent.OutboxStatus.PENDING);
        // Планировщик отработает в фоне, можно использовать Awaitility для ожидания смены статуса на SENT
    }
}