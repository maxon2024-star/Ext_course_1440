package seminars.scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import seminars.scheduler.config.MissionProperties;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(MissionProperties.class)
public class SchedulerApplication {

    public static void main(String[] args) {
        // Жестко указываем Spring'у читать файл application-scheduler.yml
        System.setProperty("spring.profiles.active", "scheduler");
        SpringApplication.run(SchedulerApplication.class, args);
    }
}