package seminars.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seminars.entity.OutboxEvent;
import java.util.List;
import java.util.UUID;

public interface OutboxRepository extends JpaRepository<OutboxEvent, UUID> {
    List<OutboxEvent> findByStatusOrderByCreatedAtAsc(OutboxEvent.OutboxStatus status);
}