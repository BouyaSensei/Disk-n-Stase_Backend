package com.stase.services;

import com.stase.dtos.librairy.GameLibraryDto;
import com.stase.dtos.librairy.LibrairyDetailDto;
import com.stase.dtos.librairy.LibrairyDto;
import com.stase.entities.Game;
import com.stase.entities.LibrairyEntry;
import com.stase.exception.GameNotFoundException;
import com.stase.exception.LibrairyAlreadyExistsException;
import com.stase.exception.LibrairyNotFoundException;
import com.stase.repositories.GameRepository;
import com.stase.repositories.LibrairyRepository;
import com.stase.repositories.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LibrairyService {

    private final LibrairyRepository librairyRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    public LibrairyService(
        LibrairyRepository librairyRepository,
        GameRepository gameRepository,
        UserRepository userRepository
    ) {
        this.librairyRepository = librairyRepository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public LibrairyDto createLibrairy(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException(
                "Utilisateur introuvable avec l'id : " + userId
            );
        }
        if (librairyRepository.findByUserId(userId).isPresent()) {
            throw new LibrairyAlreadyExistsException(userId);
        }

        LibrairyEntry entry = librairyRepository.save(new LibrairyEntry());
        librairyRepository.assignToUser(userId, entry);
        return new LibrairyDto(entry.getId());
    }

    @Transactional(readOnly = true)
    public LibrairyDetailDto getLibrairy(Long userId) {
        LibrairyEntry entry = librairyRepository
            .findByUserId(userId)
            .orElseThrow(() -> new LibrairyNotFoundException(userId));
        return toDetailDto(entry);
    }

    @Transactional
    public LibrairyDetailDto addGameToLibrairy(Long userId, Long gameId) {
        LibrairyEntry entry = librairyRepository
            .findByUserId(userId)
            .orElseThrow(() -> new LibrairyNotFoundException(userId));
        Game game = gameRepository
            .findById(gameId)
            .orElseThrow(() -> new GameNotFoundException(gameId));

        if (!game.getLibrairy().contains(entry)) {
            // mise a jour des deux cotes de la relation ManyToMany
            game.getLibrairy().add(entry);
            entry.getGames().add(game);
            gameRepository.save(game);
        }
        return toDetailDto(entry);
    }

    @Transactional
    public void removeGameFromLibrairy(Long userId, Long gameId) {
        LibrairyEntry entry = librairyRepository
            .findByUserId(userId)
            .orElseThrow(() -> new LibrairyNotFoundException(userId));
        Game game = gameRepository
            .findById(gameId)
            .orElseThrow(() -> new GameNotFoundException(gameId));

        boolean removed = game
            .getLibrairy()
            .removeIf(
                library ->
                    library.getId() != null &&
                    library.getId().equals(entry.getId())
            );
        if (removed) {
            entry
                .getGames()
                .removeIf(
                    g -> g.getId() != null && g.getId().equals(game.getId())
                );
            gameRepository.save(game);
        }
    }

    private GameLibraryDto toGameDto(Game game) {
        return new GameLibraryDto(
            game.getId(),
            game.getName(),
            game.getDescription(),
            game.getGenre(),
            game.getIsPhysical(),
            game.getCoverImageUrl(),
            game.getPlatforms()
        );
    }

    private LibrairyDetailDto toDetailDto(LibrairyEntry entry) {
        List<GameLibraryDto> games = entry
            .getGames()
            .stream()
            .map(this::toGameDto)
            .toList();
        Long userId = entry.getUser() != null ? entry.getUser().getId() : null;
        return new LibrairyDetailDto(entry.getId(), userId, games);
    }
}
