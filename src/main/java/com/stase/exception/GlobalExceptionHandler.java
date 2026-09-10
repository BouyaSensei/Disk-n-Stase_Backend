package com.stase.exception;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Couche de securite : normalise les erreurs renvoyees au client.
 * - reponses JSON previsibles, sans fuite de pile ni de details internes
 * - preserve les statuts declares via @ResponseStatus sur les exceptions du domaine
 * - herite de ResponseEntityExceptionHandler pour les erreurs HTTP standard (400, 405, 415...)
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    public record ApiError(String error) {}

    @ExceptionHandler({
        UserNotFoundException.class,
        UserAlreadyExistsException.class,
        GameNotFoundException.class,
        LibrairyNotFoundException.class,
        LibrairyAlreadyExistsException.class
    })
    public ResponseEntity<ApiError> handleDomainException(RuntimeException ex) {
        ResponseStatus declared = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);
        HttpStatus status =
            declared != null ? declared.value() : (ex instanceof GameNotFoundException ? HttpStatus.NOT_FOUND : HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(status).body(new ApiError(ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrity(DataIntegrityViolationException ex) {
        // dernier filet : doublon detecte cote base (ex. concurrence sur un username)
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(new ApiError("Conflit avec les donnees existantes"));
    }
}
