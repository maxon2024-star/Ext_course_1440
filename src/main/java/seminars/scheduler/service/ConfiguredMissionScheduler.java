package seminars.scheduler.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import seminars.dto.MissionRequest;
import seminars.scheduler.client.SpaceOperationClient;
import seminars.scheduler.config.MissionProperties;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfiguredMissionScheduler {

    private final MissionProperties properties;
    private final SpaceOperationClient client;
    private final TaskScheduler taskScheduler;

    @PostConstruct
    public void scheduleMissions() {
        log.info("=== Инициализация Планировщика Миссий ===");

        if (properties.missions() == null || properties.missions().isEmpty()) {
            log.warn("В конфигурации (application-scheduler.yml) не найдено ни одной миссии!");
            return;
        }

        for (MissionProperties.MissionConfig config : properties.missions()) {
            MissionRequest request = new MissionRequest(
                    config.targetType(),
                    config.constellationName(),
                    config.satelliteName()
            );

            Runnable task = () -> {
                log.info(">>> Сработал планировщик по CRON [{}]: Цель {}", config.cron(), config.targetType());
                client.executeMission(request);
            };

            taskScheduler.schedule(task, new CronTrigger(config.cron()));
            log.info("Запланирована миссия: Группировка '{}', Спутник '{}', Расписание: {}",
                    config.constellationName(), config.satelliteName(), config.cron());
        }
    }
}