package com.stase.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collector;

import org.springframework.stereotype.Service;

import com.stase.components.RawgProvider;
import com.stase.dtos.game.rawg.GameRawgDto;
import com.stase.dtos.game.rawg.ListGameRawgDto;
import com.stase.entities.Game;
import com.stase.repositories.GameRepository;

@Service
public class GameService {
    private GameRepository gameRepository;
    private RawgProvider rawgProvider;

    public GameService(GameRepository gameRepository, RawgProvider rawgProvider) {
        this.gameRepository = gameRepository;
        this.rawgProvider = rawgProvider;
    }

    public GameRawgDto convertToDto(Game game) {

        return new GameRawgDto(game.getId(), game.getName(), game.getDescription(), game.getGenre(),
                game.getIsPhysical());

    }

    public List<ListGameRawgDto> GetAllGames() {
        List<Game> localGames = gameRepository.findAll();
        List<ListGameRawgDto> remoteGames = rawgProvider.fetchAllGames();

        // corriger cet variable et trouver le moyen de transformer la list du repo en
        // dto
        Set<Long> localIds = localGames.stream().map(GameRawgDto::id).collect(Collector.toSet());
    }

    public GameRawgDto getGame(Long id) {

    }

    // faire la logique de fetching içi et créé un compoentn pour faire la couche de
    // creation de http pour garder la logique metier clean
}
