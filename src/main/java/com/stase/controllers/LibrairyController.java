package com.stase.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.stase.dtos.librairy.LibrairyDetailDto;
import com.stase.dtos.librairy.LibrairyDto;
import com.stase.services.LibrairyService;

@RestController
@RequestMapping("/api/librairy")
public class LibrairyController {

    private final LibrairyService librairyService;

    public LibrairyController(LibrairyService librairyService) {
        this.librairyService = librairyService;
    }

    @PostMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public LibrairyDto createLibrairy(@PathVariable Long userId) {
        return librairyService.createLibrairy(userId);
    }

    @GetMapping("/users/{userId}")
    public LibrairyDetailDto getLibrairy(@PathVariable Long userId) {
        return librairyService.getLibrairy(userId);
    }

    @PostMapping("/users/{userId}/games/{gameId}")
    public LibrairyDetailDto addGameToLibrairy(@PathVariable Long userId, @PathVariable Long gameId) {
        return librairyService.addGameToLibrairy(userId, gameId);
    }

    @DeleteMapping("/users/{userId}/games/{gameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeGameFromLibrairy(@PathVariable Long userId, @PathVariable Long gameId) {
        librairyService.removeGameFromLibrairy(userId, gameId);
    }
}
