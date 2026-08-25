package com.stase.components;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.stase.components.configurations.RawgConfiguration;
import com.stase.dtos.game.GameRawgDto;

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
    public List<GameRawgDto> fetchAllGames() {
        // faire la function fetch en se basant sur le DTO et l'api rawg
    }

    @Override
    public boolean supports() {
        return true;
    }

    @PostConstruct
    public void debug() {
        // Attention : si l'API est offline, ton app crash au démarrage à cause de ça.
        // Mais pour le debug, c'est ok.
        try {
            System.out.println("Test API RAWG : " + fetchAllGames());
        } catch (Exception e) {
            System.err.println("Erreur lors du debug @PostConstruct : " + e.getMessage());
        }
    }
}
