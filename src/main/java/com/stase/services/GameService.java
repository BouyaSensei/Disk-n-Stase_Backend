
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
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

    public String getAllGames() {

        List<Game> localGames = gameRepository.findAll();
        List<ListGameRawgDto> remoteGames = rawgProvider.fetchAllGames();

        // corriger cet variable et trouver le moyen de transformer la list du repo en
        // dto
        if (localGames.isEmpty()) {
            // List<Game> gameToAdd =
            try {
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

    public GameRawgDto getGame(Long id) {

    }

    // faire la logique de fetching içi et créé un compoentn pour faire la couche de
    // creation de http pour garder la logique metier clean
}
