package seminars.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutboxEvent {
    @Id
    private UUID id = UUID.randomUUID();

    @Column(name = "aggregate_id")
    private String aggregateId;

    @Column(name = "event_type")
    private String eventType;

    @Column(columnDefinition = "jsonb")
    private String payload;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    @Enumerated(EnumType.STRING)
    private OutboxStatus status = OutboxStatus.PENDING;

    public enum OutboxStatus {
        PENDING, SENT
    }
}