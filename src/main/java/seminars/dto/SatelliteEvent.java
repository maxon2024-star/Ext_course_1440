package seminars.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SatelliteEvent {
    private String eventType;
    private String constellationName;
    private String satelliteDetails;
    private UUID eventId = UUID.randomUUID();
}