package com.stase.components;

import java.net.URI;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.stase.components.configurations.RawgConfiguration;
import com.stase.dtos.game.rawg.GameRawgDto;
import com.stase.dtos.game.rawg.ListGameRawgDto;
import com.stase.dtos.response.rawg.RawgResponse;

import jakarta.annotation.PostConstruct;

@Component
public class RawgProvider implements GameProvider {

    // Utilise 'final' pour les dépendances injectées (bonne pratique)
    private final RestClient restClient;
    private final RawgConfiguration config;

    public RawgProvider(RestClient restClient, RawgConfiguration config) {
        this.restClient = restClient;
        this.config = config;
    }

    @Override
    public List<ListGameRawgDto> fetchAllGames() {
        // faire la function fetch en se basant sur le DTO et l'api rawg
        /*
         * GameRawgDto games = restClient.get().uri(uri ->
         * uri.uri(config.getRawgApiKey()).path("/games")
         * .queryParam("key", config.getRawgApiKey())
         * .queryParam("count", 10))
         * .retrieve()
         * .body(GameRawgDto.class);
         * return games != null ? games.getResults() : List.of();
         */

        URI urlCleaner = UriComponentsBuilder.fromUriString(config.getRawgUrl() + "/games").queryParam("key",
                config.getRawgApiKey()).encode().build().toUri();

        RawgResponse response = restClient.get().uri(urlCleaner).retrieve().body(RawgResponse.class);
        List<ListGameRawgDto> games = response.results();
        return games != null ? games : List.of();
    }

    @Override
    public GameRawgDto gameDetail(Long id) {
        String urlCleaner = UriComponentsBuilder.fromUriString(config.getRawgUrl() + "/games/" + id)
                .queryParam("key",
                        config.getRawgApiKey())
                .encode().build().toString();
        GameRawgDto game = restClient.get().uri(urlCleaner).retrieve().body(GameRawgDto.class);

        return game;
    }

    public String uriTest() {
        String urlCleaner = UriComponentsBuilder.fromUriString(config.getRawgUrl() + "/games" + "/3194")
                .queryParam("key",
                        config.getRawgApiKey())
                .encode().build().toString();
        return urlCleaner;
    };

    @Override
    public boolean supports() {
        return true;
    }

    @PostConstruct
    public void debug() {
        // Attention : si l'API est offline, ton app crash au démarrage à cause de ça.
        // Mais pour le debug, c'est ok.
        try {
            System.out.println("Test API RAWG : " + gameDetail(3478L));
        } catch (Exception e) {
            System.err.println("Erreur lors du debug @PostConstruct : " + e.getMessage());
            System.err.println("Erreur lors du debug @PostConstruct avec l'uri : " + uriTest());

        }
    }
}
