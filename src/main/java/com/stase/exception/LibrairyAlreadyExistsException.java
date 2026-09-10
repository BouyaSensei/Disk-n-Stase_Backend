package com.stase.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class LibrairyAlreadyExistsException extends RuntimeException {
    public LibrairyAlreadyExistsException(Long userId) {
        super("L'utilisateur avec l'id : " + userId + " possede deja une librairie");
    }
}
