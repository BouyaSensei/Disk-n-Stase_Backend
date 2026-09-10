package com.stase.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LibrairyNotFoundException extends RuntimeException {
    public LibrairyNotFoundException(Long userId) {
        super("Aucune librairie trouvee pour l'utilisateur avec l'id : " + userId);
    }
}
