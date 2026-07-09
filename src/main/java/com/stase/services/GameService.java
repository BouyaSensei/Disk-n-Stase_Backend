package com.stase.services;

import org.springframework.stereotype.Service;

import com.stase.repositories.GameRepository;

@Service
public class GameService {
    private GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

}
