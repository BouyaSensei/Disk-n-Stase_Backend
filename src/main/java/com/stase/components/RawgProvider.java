package com.stase.components;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import com.stase.components.configurations.RawgConfiguration;
import com.stase.dtos.game.rawg.GameRawgDto;
import com.stase.dtos.game.rawg.ListGameRawgDto;
import com.stase.dtos.response.rawg.RawgResponse;
import com.stase.exception.GameNotFoundException;

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

        URI urlCleaner = UriComponentsBuilder.fromUriString(config.getRawgUrl() + "/games").queryParam("key",
                config.getRawgApiKey()).encode().build().toUri();

        RawgResponse response = restClient.get().uri(urlCleaner).retrieve().body(RawgResponse.class);
        List<ListGameRawgDto> games = response.results();
        return games != null ? games : List.of();
    }

    @Override
    public GameRawgDto gameDetail(Long id) {
        String urlCleaner = UriComponentsBuilder.fromUriString(config.getRawgUrl() + "/games/{id}")
                .queryParam("key",
                        config.getRawgApiKey())
                .encode().build(id).toString();
        // logique de retour du jeu
        return restClient.get().uri(urlCleaner).retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                    if (res.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                        throw new GameNotFoundException(id);
                    }
                    throw new RestClientException("Erreur inattendue Rawg : " + res.getStatusCode());
                }).body(GameRawgDto.class);

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
            System.out.println("la taille de la liste : " + fetchAllGames().size());
        } catch (Exception e) {
            System.err.println("Erreur lors du debug @PostConstruct : " + e.getMessage());
            System.err.println("Erreur lors du debug @PostConstruct avec l'uri : " + uriTest());

        }
    }
}
