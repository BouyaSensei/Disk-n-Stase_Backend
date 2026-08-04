package com.stase.components.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RawgConfiguration {

    @Value("${spring.external-api.rawg-api}")
    private String rawgApiKey;

    @Value("${spring.external-api.rawg-url}")
    private String rawgUrl;

    @Bean
    /*
     * public RestClient rawgRestClient() {
     * return RestClient.builder()
     * .baseUrl(rawgUrl)
     * .defaultHeader("Accept", "application/json")
     * .defaultStatusHandler((status) -> status.is4xxClientError() ||
     * status.is5xxServerError(),
     * (request, response) -> {
     * throw new ApiException("RAWG API error: " + response.getStatusCode());
     * })
     * .build();
     * }
     */

    public String getRawgApiKey() {
        return rawgApiKey;
    }

    public String getRawgUrl() {
        return rawgUrl;
    }
}
