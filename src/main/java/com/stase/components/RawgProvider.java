package com.stase.components;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.stase.dtos.game.GameDto;

@Component
public class RawgProvider implements GameProvider {
    private RestClient restClient;

    public RawgProvider(RestClient restClient) {
        this.restClient = restClient;

    }

    public List<GameDto> fetchAllGames() {
        return restClient.get().uri("/games").retrieve().body(new ParameterizedTypeReference<List<GameDto>>() {
        });

    };

    public boolean supports() {
        return true;
    };
}
