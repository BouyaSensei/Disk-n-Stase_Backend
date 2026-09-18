package com.stase.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.stase.exception.GameNotFoundException;
import com.stase.exception.LibrairyAlreadyExistsException;
import com.stase.exception.LibrairyNotFoundException;
import com.stase.services.LibrairyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests des cas d'echec : les exceptions du service doivent remonter telles quelles
 * (GlobalExceptionHandler les convertit ensuite en reponses HTTP 404 / 409)
 */
@ExtendWith(MockitoExtension.class)
class LibrairyControllerTest {

    @Mock
    private LibrairyService librairyService;

    private LibrairyController controller;

    @BeforeEach
    void setUp() {
        controller = new LibrairyController(librairyService);
    }

    @Test
    void createLibrairyAvecUserIntrouvableDoitLancerIllegalArgumentException() {
        when(librairyService.createLibrairy(99L)).thenThrow(
            new IllegalArgumentException("Utilisateur introuvable avec l'id : 99")
        );

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> controller.createLibrairy(99L)
        );
        assertEquals("Utilisateur introuvable avec l'id : 99", ex.getMessage());
    }

    @Test
    void createLibrairyDejaExistanteDoitLancerLibrairyAlreadyExistsException() {
        when(librairyService.createLibrairy(1L)).thenThrow(
            new LibrairyAlreadyExistsException(1L)
        );

        LibrairyAlreadyExistsException ex = assertThrows(
            LibrairyAlreadyExistsException.class,
            () -> controller.createLibrairy(1L)
        );
        assertEquals(
            "L'utilisateur avec l'id : 1 possede deja une librairie",
            ex.getMessage()
        );
    }

    @Test
    void getLibrairyIntrouvableDoitLancerLibrairyNotFoundException() {
        when(librairyService.getLibrairy(99L)).thenThrow(
            new LibrairyNotFoundException(99L)
        );

        LibrairyNotFoundException ex = assertThrows(
            LibrairyNotFoundException.class,
            () -> controller.getLibrairy(99L)
        );
        assertEquals(
            "Aucune librairie trouvee pour l'utilisateur avec l'id : 99",
            ex.getMessage()
        );
    }

    @Test
    void addGameToLibrairyAvecLibrairyIntrouvableDoitLancerLibrairyNotFoundException() {
        when(librairyService.addGameToLibrairy(99L, 1L)).thenThrow(
            new LibrairyNotFoundException(99L)
        );

        assertThrows(
            LibrairyNotFoundException.class,
            () -> controller.addGameToLibrairy(99L, 1L)
        );
    }

    @Test
    void addGameToLibrairyAvecJeuIntrouvableDoitLancerGameNotFoundException() {
        when(librairyService.addGameToLibrairy(1L, 42L)).thenThrow(
            new GameNotFoundException(42L)
        );

        GameNotFoundException ex = assertThrows(
            GameNotFoundException.class,
            () -> controller.addGameToLibrairy(1L, 42L)
        );
        assertEquals("Jeu introuvable en local avec l'id :42", ex.getMessage());
    }

    @Test
    void removeGameFromLibrairyAvecLibrairyIntrouvableDoitLancerLibrairyNotFoundException() {
        doThrow(new LibrairyNotFoundException(99L))
            .when(librairyService)
            .removeGameFromLibrairy(99L, 1L);

        assertThrows(
            LibrairyNotFoundException.class,
            () -> controller.removeGameFromLibrairy(99L, 1L)
        );
    }

    @Test
    void removeGameFromLibrairyAvecJeuIntrouvableDoitLancerGameNotFoundException() {
        doThrow(new GameNotFoundException(42L))
            .when(librairyService)
            .removeGameFromLibrairy(1L, 42L);

        assertThrows(
            GameNotFoundException.class,
            () -> controller.removeGameFromLibrairy(1L, 42L)
        );
    }
}
