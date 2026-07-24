package com.stase.components;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@ConfigurationProperties(prefix = "spring.external-api")
@Component

public class CloudGameFetching {
    private String rawgApi;

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
