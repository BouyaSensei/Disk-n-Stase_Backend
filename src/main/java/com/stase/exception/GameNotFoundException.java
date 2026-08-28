package com.stase.exception;

public class GameNotFoundException extends RuntimeException {

    public GameNotFoundException(Long id) {
        super("Jeu introuvable avec l'id :" + id);
    }
}
