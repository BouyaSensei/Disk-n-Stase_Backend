package com.stase.services;

import com.stase.dtos.librairy.LibrairyDto;
import com.stase.dtos.user.UserCreateDto;
import com.stase.dtos.user.UserDto;
import com.stase.dtos.user.UserUpdateDto;
import com.stase.entities.Game;
import com.stase.entities.LibrairyEntry;
import com.stase.entities.User;
import com.stase.exception.UserAlreadyExistsException;
import com.stase.exception.UserNotFoundException;
import com.stase.repositories.GameRepository;
import com.stase.repositories.LibrairyRepository;
import com.stase.repositories.UserRepository;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final LibrairyRepository librairyRepository;
    private final GameRepository gameRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
        UserRepository userRepository,
        LibrairyRepository librairyRepository,
        GameRepository gameRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.librairyRepository = librairyRepository;
        this.gameRepository = gameRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserDto createUser(UserCreateDto dto) {
        if (userRepository.existsByUsernameIgnoreCase(dto.username())) {
            throw new UserAlreadyExistsException(
                "Le nom d'utilisateur est deja pris : " + dto.username()
            );
        }
        if (userRepository.existsByEmailIgnoreCase(dto.email())) {
            throw new UserAlreadyExistsException(
                "L'email est deja utilise : " + dto.email()
            );
        }

        // le mot de passe n'est jamais stocke en clair
        User user = userRepository.save(
            new User(
                dto.username(),
                passwordEncoder.encode(dto.password()),
                dto.email()
            )
        );

        // chaque utilisateur recoit sa propre librairie, attribuee personnellement
        LibrairyEntry entry = librairyRepository.save(new LibrairyEntry());
        librairyRepository.assignToUser(user.getId(), entry);

        return new UserDto(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            new LibrairyDto(entry.getId())
        );
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public UserDto getUser(Long id) {
        return toDto(findUser(id));
    }

    @Transactional
    public UserDto updateUser(Long id, UserUpdateDto dto) {
        User user = findUser(id);

        if (
            dto.username() != null &&
            !dto.username().equalsIgnoreCase(user.getUsername()) &&
            userRepository.existsByUsernameIgnoreCase(dto.username())
        ) {
            throw new UserAlreadyExistsException(
                "Le nom d'utilisateur est deja pris : " + dto.username()
            );
        }
        if (
            dto.email() != null &&
            !dto.email().equalsIgnoreCase(user.getEmail()) &&
            userRepository.existsByEmailIgnoreCase(dto.email())
        ) {
            throw new UserAlreadyExistsException(
                "L'email est deja utilise : " + dto.email()
            );
        }

        if (dto.username() != null) {
            user.setUsername(dto.username());
        }
        if (dto.email() != null) {
            user.setEmail(dto.email());
        }
        if (dto.password() != null) {
            user.setPassword(passwordEncoder.encode(dto.password()));
        }

        return toDto(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = findUser(id);
        LibrairyEntry entry = user.getLibrairyEntry();
        if (entry != null) {
            // la table de jointure library_game est geree cote Jeu :
            // on retire d'abord la librairie de chaque jeu pour ne pas laisser d'orphelins
            for (Game game : entry.getGames()) {
                game.getLibrairy().removeIf(
                    library ->
                        library.getId() != null &&
                        library.getId().equals(entry.getId())
                );
                gameRepository.save(game);
            }
            gameRepository.flush();
        }
        // on supprime d'abord l'utilisateur (qui porte la FK), puis sa librairie
        userRepository.delete(user);
        if (entry != null) {
            librairyRepository.delete(entry);
        }
    }

    private User findUser(Long id) {
        return userRepository
            .findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
    }

    private UserDto toDto(User user) {
        LibrairyDto librairy =
            user.getLibrairyEntry() != null
                ? new LibrairyDto(user.getLibrairyEntry().getId())
                : null;
        return new UserDto(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            librairy
        );
    }
}
