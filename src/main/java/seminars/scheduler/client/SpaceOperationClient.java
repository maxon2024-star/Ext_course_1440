package seminars.scheduler.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import seminars.dto.MissionRequest;

@Slf4j
@Component
@RequiredArgsConstructor
public class SpaceOperationClient {

    private final RestClient restClient;

    public void executeMission(MissionRequest request) {
        try {
            log.info("Отправка запроса на выполнение миссии: {}", request);
            String response = restClient.post()
                    .uri("/missions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(String.class);

            log.info("Успешный ответ от основного сервиса: {}", response);
        } catch (Exception e) {
            log.error("Ошибка при вызове основного сервиса! Убедись, что Main запущен. Детали: {}", e.getMessage());
        }
    }
}