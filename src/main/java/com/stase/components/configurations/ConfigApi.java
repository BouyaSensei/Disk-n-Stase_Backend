package com.stase.components.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration

public class ConfigApi {
    @Bean
    public RestClient restClient() {
        return RestClient.create();
    }
}
