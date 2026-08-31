package com.stase.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
                game.getIsPhysical(), game.getCoverImageUrl());

    }

    public Game converToGame(GameRawgDto dto) {
        Game gameFresh = new Game();
        gameFresh.setId(dto.id());
        gameFresh.setName(dto.name());
        gameFresh.setDescription(dto.description());

        return gameFresh;

        // return new Game(Game.setId(gameDto.id()));
    }

    public String listToJson(List<ListGameRawgDto> games) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(games);
    }

    public String getAllGames() {

        List<Game> localGames = gameRepository.findAll();
        List<ListGameRawgDto> remoteGames = rawgProvider.fetchAllGames();

        // corriger cet variable et trouver le moyen de transformer la list du repo en
        // dto
        if (localGames.isEmpty()) {
            return listToJson(remoteGames);

        } else {
            Set<Long> localIds = localGames.stream().map((g) -> convertToDto(g)).map(GameRawgDto::id)
                    .collect(Collectors.toSet());
            List<ListGameRawgDto> newGames = remoteGames.stream().filter(game -> !localIds.contains(game.id()))
                    .collect(Collectors.toList());
            return listToJson(newGames);
        }

    }

    public GameRawgDto getGame(Long id) {

    }

    // faire la logique de fetching içi et créé un compoentn pour faire la couche de
    // creation de http pour garder la logique metier clean
}
