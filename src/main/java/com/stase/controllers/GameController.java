package com.stase.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stase.services.GameService;

@RestController
@RequestMapping("/api")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/games")
    public String getAllGames() {
        return gameService.getAllGames();
    }

    @GetMapping("/games/{id}")
    public String getGameDetail(@PathVariable Long id) {

        return gameService.getGame(id);
    }

}
