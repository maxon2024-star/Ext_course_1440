package seminars.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MissionRequest {
    private TargetType targetType;
    private String constellationName;
    private String satelliteName; // Обязательно только для SINGLE_SATELLITE

    public enum TargetType {
        CONSTELLATION,
        SINGLE_SATELLITE
    }
}