package com.stase.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.stase.exception.GameNotFoundException;
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
        when(gameService.getGame(42L, true)).thenReturn(
            "{\"id\":42,\"name\":\"Game C\"}"
        );

        String result = controller.getGameDetail(42L);

        assertEquals("{\"id\":42,\"name\":\"Game C\"}", result);
        verify(gameService).getGame(42L, true);
    }

    // --- Tests des cas d'echec : les exceptions du service doivent remonter telles quelles
    // (GlobalExceptionHandler les convertit ensuite en reponses HTTP) ---

    @Test
    void getGameDetailJeuIntrouvableDoitLancerGameNotFoundException() {
        when(gameService.getGame(42L, true)).thenThrow(
            new GameNotFoundException(42L)
        );

        GameNotFoundException ex = assertThrows(
            GameNotFoundException.class,
            () -> controller.getGameDetail(42L)
        );
        assertEquals("Jeu introuvable en local avec l'id :42", ex.getMessage());
    }

    @Test
    void getAllGamesErreurApiDistanteDoitPropagerRuntimeException() {
        when(gameService.getAllGames(1L)).thenThrow(
            new RuntimeException(
                "erreur lors de la lecture de la liste de jeux a distance"
            )
        );

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
            controller.getAllGames(null)
        );
        assertEquals(
            "erreur lors de la lecture de la liste de jeux a distance",
            ex.getMessage()
        );
    }
}
