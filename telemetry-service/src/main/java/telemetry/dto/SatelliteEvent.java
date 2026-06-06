package telemetry.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SatelliteEvent {
    private String eventType; // "CREATED" или "DELETED"
    private String constellationName;
    private String satelliteDetails;
}