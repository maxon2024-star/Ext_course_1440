package seminars.scheduler.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient spaceOperationRestClient(MissionProperties properties) {
        return RestClient.builder()
                .baseUrl(properties.url())
                .build();
    }
}