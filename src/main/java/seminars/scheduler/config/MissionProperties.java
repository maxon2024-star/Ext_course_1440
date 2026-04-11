package seminars.scheduler.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import seminars.dto.MissionRequest;

import java.util.List;

@ConfigurationProperties(prefix = "app.space-center-service")
public record MissionProperties(String url, List<MissionConfig> missions) {
    public record MissionConfig(
            MissionRequest.TargetType targetType,
            String constellationName,
            String satelliteName,
            String cron
    ) {}
}