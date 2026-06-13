package telemetry.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "inbox")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InboxEvent {
    @Id
    private UUID eventId;
    private String aggregateId;
    private String eventType;
    private Instant processedAt = Instant.now();
}