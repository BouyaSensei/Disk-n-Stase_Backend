package com.stase.services;

import org.springframework.stereotype.Service;

import com.stase.dtos.game.GameDto;
import com.stase.entities.Game;
import com.stase.repositories.GameRepository;

@Service
public class GameService {
    private GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    private GameDto convertToDto(Game game) {
        return new GameDto(game.getId(), game.getTitle(), game.getDescription(), game.getGenre(), game.getIsPhysical());
    }
    // faire la logique de fetching içi et créé un compoentn pour faire la couche de
    // creation de http pour garder la logique metier clean
}
