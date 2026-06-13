package telemetry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telemetry.entity.InboxEvent;
import java.util.UUID;

public interface InboxRepository extends JpaRepository<InboxEvent, UUID> {
}