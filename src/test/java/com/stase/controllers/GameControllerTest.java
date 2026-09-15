package com.stase.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.stase.services.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GameControllerTest {

    @Mock
    private GameService gameService;

    private GameController controller;

    @BeforeEach
    void setUp() {
        controller = new GameController(gameService);
    }

    @Test
    void getAllGamesSansPageDoitDeleguerAPage1() {
        when(gameService.getAllGames(1L)).thenReturn("[{\"name\":\"Game A\"}]");

        String result = controller.getAllGames(null);

        assertEquals("[{\"name\":\"Game A\"}]", result);
        verify(gameService).getAllGames(1L);
    }

    @Test
    void getAllGamesAvecPageInvalideDoitDeleguerAPage1() {
        when(gameService.getAllGames(1L)).thenReturn("[]");

        String result = controller.getAllGames(0L);

        assertEquals("[]", result);
        verify(gameService).getAllGames(1L);
    }

    @Test
    void getAllGamesAvecPageNegativeDoitDeleguerAPage1() {
        when(gameService.getAllGames(1L)).thenReturn("[]");

        String result = controller.getAllGames(-5L);

        assertEquals("[]", result);
        verify(gameService).getAllGames(1L);
    }

    @Test
    void getAllGamesAvecPageValideDoitDeleguerLaPage() {
        when(gameService.getAllGames(3L)).thenReturn("[{\"name\":\"Game B\"}]");

        String result = controller.getAllGames(3L);

        assertEquals("[{\"name\":\"Game B\"}]", result);
        verify(gameService).getAllGames(3L);
    }

    @Test
    void getGameDetailDoitDeleguerAvecRemoteATrue() {
        when(gameService.getGame(42L, true)).thenReturn("{\"id\":42,\"name\":\"Game C\"}");

        String result = controller.getGameDetail(42L);

        assertEquals("{\"id\":42,\"name\":\"Game C\"}", result);
        verify(gameService).getGame(42L, true);
    }
}
