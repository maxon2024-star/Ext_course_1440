package seminars.dto;

import lombok.Data;

@Data
public class MissionRequest {
    private String constellationName;
    // Можно добавить дополнительные поля, например targetOrbit или missionType
}