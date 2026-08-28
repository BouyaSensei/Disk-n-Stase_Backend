package com.stase.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.stase.dtos.game.rawg.GameRawgDto;
import com.stase.dtos.game.rawg.ListGameRawgDto;
import com.stase.entities.Game;
import com.stase.repositories.GameRepository;

@Service
public class GameService {
    private GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public GameRawgDto convertToDto(Game game) {

        return new GameRawgDto(game.getId(), game.getName(), game.getDescription(), game.getGenre(),
                game.getIsPhysical());

    }

    public List<ListGameRawgDto> GetAllGames() {

    }

    public GameRawgDto getGame() {

    }

    // faire la logique de fetching içi et créé un compoentn pour faire la couche de
    // creation de http pour garder la logique metier clean
}
