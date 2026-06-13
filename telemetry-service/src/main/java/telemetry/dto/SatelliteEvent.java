package telemetry.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SatelliteEvent {
    private String eventType; // "CREATED" или "DELETED"
    private String constellationName;
    private String satelliteDetails; // Здесь мы передаем ID спутника
    private UUID eventId; // Ключевое поле для Inbox
}