package com.stase.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stase.components.RawgProvider;
import com.stase.dtos.game.rawg.GameRawgDto;
import com.stase.dtos.game.rawg.GenreRawgDto;
import com.stase.dtos.game.rawg.ListGameRawgDto;
import com.stase.entities.Game;
import com.stase.repositories.GameRepository;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final RawgProvider rawgProvider;

    public GameService(GameRepository gameRepository, RawgProvider rawgProvider) {
        this.gameRepository = gameRepository;
        this.rawgProvider = rawgProvider;
    }

    public String dtoEmbasement(GameRawgDto dto) {

        Game gameConvert = new Game();

        gameConvert.setId(dto.id());
        gameConvert.setName(dto.name());
        gameConvert.setDescription(dto.description());
        // gameConvert.setGenre(dto.genres());
        try {
            return gameToJson(dto);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("erreur lors de la conversion en json du jeu");
        }

    }

    public List<String> converToList(List<GenreRawgDto> list) throws JsonProcessingException {
        List<String> listConverter = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        for (GenreRawgDto genre : list) {
            try {

                listConverter.add(mapper.writeValueAsString(genre));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Erreur conversion JSON", e);
            }

        }
        return listConverter;
    }

    public GameRawgDto convertToDto(Game game) {

        return new GameRawgDto(game.getId(), game.getName(), game.getDescription(), game.getGenre(),
                game.getIsPhysical(), game.getCoverImageUrl());

    }

    public Game converToGame(ListGameRawgDto dto) {
        Game gameFresh = new Game();
        gameFresh.setId(dto.id());
        gameFresh.setName(dto.name());
        gameFresh.setDescription(dto.description());
        return gameFresh;

    }

    public List<Game> convertToListGame(List<ListGameRawgDto> dto) {
        List<Game> convertGames = new ArrayList<>();
        for (ListGameRawgDto game : dto) {

            Game gameConvert = new Game();
            gameConvert.setId(game.id());
            gameConvert.setName(game.name());
            gameConvert.setDescription(game.description());
            // convertir cete liste de genre en json
            try {
                gameConvert.setGenre(converToList(game.genres()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Erreur conversion JSON", e);
            }
            convertGames.add(gameConvert);

        }
        return convertGames;
    }

    public String listToJson(List<ListGameRawgDto> games) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(games);
    }

    public String gameToJson(GameRawgDto game) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(game);
    }

    public String getAllGames() {

        List<Game> localGames = gameRepository.findAll();
        List<ListGameRawgDto> remoteGames = rawgProvider.fetchAllGames();

        // corriger cet variable et trouver le moyen de transformer la list du repo en
        // dto
        if (localGames.isEmpty()) {
            // List<Game> gameToAdd =
            try {
                // faire l'embasement de la db ici
                remoteGames.stream().map(this::converToGame).toList();
                return listToJson(remoteGames);
            } catch (IOException e) {
                throw new RuntimeException("erreur lors de la lecture de la liste de jeux à distance", e);
            }

        } else {
            Set<Long> localIds = localGames.stream().map(this::convertToDto).map(GameRawgDto::id)
                    .collect(Collectors.toSet());
            try {
                List<ListGameRawgDto> newGames = remoteGames.stream().filter(game -> !localIds.contains(game.id()))
                        .collect(Collectors.toList());
                return listToJson(newGames);
            } catch (IOException e) {
                throw new RuntimeException("erreur lors de la creation des jeux en db", e);
            }

        }

    }

    public String getGame(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'id ne peut pas être null");
        }
        Optional<Game> optionnalGame = gameRepository.findById(id);
        if (optionnalGame.isPresent()) {
            Game findGame = optionnalGame.get();
            try {
                GameRawgDto gameDto = convertToDto(findGame);
                return gameToJson(gameDto);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("erreur lors de la conversation du jeu en json", e);
            }

        } else {
            try {
                ObjectMapper mapper = new ObjectMapper();

                JsonNode node = mapper.readTree(rawgProvider.gameDetail(id));
                Game newGame = new Game();
                newGame.setId(node.get("id").asLong());
                newGame.setName(node.get("name").asText());
                newGame.setDescription(node.get("description").asText());
                newGame.setCoverImageUrl(node.get("background_image").asText());
                newGame.setPlatforms(node.get("platforms").asText());
                return rawgProvider.gameDetail(id);
                // GameRawgDto remoteGame = ;
                // embasement
                //
                // return dtoEmbasement(remoteGame);

            } catch (Exception e) {
                throw new RuntimeException("erreur de la recup du jeu sur le rawg", e);
            }

        }

        // on recupere le jeu par id sur la db et si il n'existe verifier sur la remote
        // et ensuite le creer

        // on fait ensuite la convertion du dto en list et si local de l'entity au dto
        // ect

        // et on return
    }

    // faire la logique de fetching içi et créé un compoentn pour faire la couche de
    // creation de http pour garder la logique metier clean
}
