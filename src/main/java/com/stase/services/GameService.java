package com.stase.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stase.components.RawgProvider;
import com.stase.dtos.game.rawg.GameRawgDto;
import com.stase.dtos.game.rawg.GenreRawgDto;
import com.stase.dtos.game.rawg.ListGameRawgDto;
import com.stase.entities.Check_physique;
import com.stase.entities.Game;
import com.stase.exception.GameNotFoundException;
import com.stase.repositories.GameFilterRepository;
import com.stase.repositories.GameRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final RawgProvider rawgProvider;
    private final GameFilterRepository gameFilterRepository;

    public GameService(
        GameRepository gameRepository,
        RawgProvider rawgProvider,
        GameFilterRepository gameFilterRepository
    ) {
        this.gameRepository = gameRepository;
        this.rawgProvider = rawgProvider;
        this.gameFilterRepository = gameFilterRepository;
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
            throw new IllegalStateException(
                "erreur lors de la conversion en json du jeu"
            );
        }
    }

    public List<String> converToList(List<GenreRawgDto> list)
        throws JsonProcessingException {
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
        return new GameRawgDto(
            game.getId(),
            game.getName(),
            game.getDescription(),
            game.getGenre(),
            game.getIsPhysical(),
            game.getPlatforms(),
            game.getCoverImageUrl()
        );
    }

    public Game converToGame(ListGameRawgDto dto) {
        Game gameFresh = new Game();
        gameFresh.setRawgId(dto.id());
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

    public String listToJson(List<ListGameRawgDto> games)
        throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(games);
    }

    public String gameToJson(GameRawgDto game) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(game);
    }

    public String getAllGames(Long page) {
        List<Game> localGames = gameRepository.findAll();

        List<ListGameRawgDto> remoteGames = rawgProvider.fetchAllGames(page);

        // corriger cet variable et trouver le moyen de transformer la list du repo en
        // dto
        if (localGames.isEmpty()) {
            // List<Game> gameToAdd =
            try {
                // faire l'embasement de la db ici
                remoteGames.forEach(game -> getGame(game.id(), true));
                return listToJson(remoteGames);
            } catch (IOException e) {
                throw new RuntimeException(
                    "erreur lors de la lecture de la liste de jeux à distance",
                    e
                );
            }
        } else {
            // les ids locaux à comparer sont les rawgId, pas les ids auto-générés
            Set<Long> localRawgIds = localGames
                .stream()
                .map(Game::getRawgId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
            List<ListGameRawgDto> newGames = remoteGames
                .stream()
                .filter(game -> !localRawgIds.contains(game.id()))
                .toList();
            // getGame s'occupe de l'embase de chaque jeu manquant
            for (ListGameRawgDto game : newGames) {
                getGame(game.id(), true);
            }
            try {
                return listToJson(newGames);
            } catch (IOException e) {
                throw new RuntimeException(
                    "erreur lors de la creation des jeux en db",
                    e
                );
            }
        }
    }

    public String getGame(Long id, boolean remote) {
        if (id == null) {
            throw new IllegalArgumentException("L'id ne peut pas être null");
        }
        Optional<Game> optionnalGame;
        if (remote) {
            optionnalGame = gameRepository.findByRawgId(id);
        } else {
            optionnalGame = gameRepository.findById(id);
        }

        if (optionnalGame.isPresent()) {
            Game findGame = optionnalGame.get();
            try {
                GameRawgDto gameDto = convertToDto(findGame);
                return gameToJson(gameDto);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(
                    "erreur lors de la conversation du jeu en json",
                    e
                );
            }
        } else {
            if (!remote) {
                throw new GameNotFoundException(id);
            }
            try {
                ObjectMapper mapper = new ObjectMapper();
                // il faut trouver une façon de faire un id sur ou un nom pour faire la
                // recherche en remote;
                JsonNode node = mapper.readTree(rawgProvider.gameDetail(id));
                Game newGame = new Game();
                newGame.setName(node.get("name").asText());
                newGame.setDescription(node.get("description").asText());
                newGame.setCoverImageUrl(node.get("background_image").asText());
                newGame.setPlatforms(
                    mapper.writeValueAsString(node.get("platforms"))
                );
                newGame.setRawgId(id);
                gameRepository.save(newGame);
                // retourner le localid avec l'objet
                // return rawgProvider.gameDetail(id);
                return gameToJson(convertToDto(newGame));
                // GameRawgDto remoteGame = ;
                // embasement
                //
                // return dtoEmbasement(remoteGame);
            } catch (Exception e) {
                throw new RuntimeException(
                    "erreur de la recup du jeu sur le rawg",
                    e
                );
            }
        }

        // on recupere le jeu par id sur la db et si il n'existe verifier sur la remote
        // et ensuite le creer

        // on fait ensuite la convertion du dto en list et si local de l'entity au dto
        // ect

        // et on return
    }

    public int applyPhysical() {
        List<Check_physique> physicalList = gameFilterRepository.findAll();
        List<Game> localGames = gameRepository.findAll();

        if (localGames.isEmpty()) {
            // aucun jeu en base, rien à mettre à jour
            return 404;
        }

        List<Game> gameApply = new ArrayList<>();
        for (Game game : localGames) {
            for (Check_physique check_physique : physicalList) {
                if (
                    Objects.equals(
                        check_physique.getGame_title().trim().toLowerCase(),
                        game.getName().trim().toLowerCase()
                    )
                ) {
                    game.setIsPhysical(true);
                    gameApply.add(game);
                    break;
                }
            }
        }

        if (gameApply.isEmpty()) {
            // aucun titre de la liste physique ne correspond à un jeu local
            return 404;
        }

        try {
            gameRepository.saveAll(gameApply);
        } catch (Exception e) {
            // l'update en base a échoué
            return 500;
        }
        return 200;
    }
}
