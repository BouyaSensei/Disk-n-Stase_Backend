package com.stase.components;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.stase.components.configurations.RawgConfiguration;

import jakarta.annotation.PostConstruct;

@ConfigurationProperties(prefix = "spring.external-api")
@Component

public class CloudGameFetching {
    private String rawgApi;
    private RestClient rawgRestClient;

    public CloudGameFetching(RestClient rawgRestClient, RawgConfiguration config) {
        this.rawgRestClient = rawgRestClient;
        this.rawgApi = config.getRawgApiKey();
    }

    public void setRawgApi(String rawgApi) {
        this.rawgApi = rawgApi;
    }

    public String getRawgApi() {
        return rawgApi;
    }

    @PostConstruct
    public void debug() {
        System.out.println(rawgApi);

    }

}
