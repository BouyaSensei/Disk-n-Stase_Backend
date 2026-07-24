package com.stase.components.configurations;

import com.stase.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RawgConfiguration {

    @Value("${spring.external-api.rawg-api}")
    private String rawgApiKey;

    @Bean
    public RestClient rawgRestClient() {
        return RestClient.builder()
                .baseUrl("https://api.rawg.io/api")
                .defaultHeader("Accept", "application/json")
                .defaultStatusHandler((status) -> status.is4xxClientError() || status.is5xxServerError(),
                        (request, response) -> {
                            throw new ApiException("RAWG API error: " + response.getStatusCode());
                        })
                .build();
    }

    public String getRawgApiKey() {
        return rawgApiKey;
    }
}
