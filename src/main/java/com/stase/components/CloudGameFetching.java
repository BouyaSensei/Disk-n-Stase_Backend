package com.stase.components;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@ConfigurationProperties(prefix = "spring.external-api")
@Component

public class CloudGameFetching {
    private String igdbUrl;

    public void setIgdbUrl(String igdbUrl) {
        this.igdbUrl = igdbUrl;
    }

    public String getIgdbUrl() {
        return igdbUrl;
    }

    @PostConstruct
    public void debug() {
        System.out.println(igdbUrl);

    }

}
