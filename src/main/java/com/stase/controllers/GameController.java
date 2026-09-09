package com.stase.controllers;

import com.stase.services.GameService;
import java.security.KeyStore.PasswordProtection;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/games/page={page}")
    //envoye rles variable de security
    public String getAllGames(@PathVariable(required = false) Long page) {
        if (page == null || page < 1) {
            page = 1L;
        }

        return gameService.getAllGames(page);
    }

    @GetMapping("/games/{id}")
    public String getGameDetail(@PathVariable Long id) {
        return gameService.getGame(id, true);
    }
}
