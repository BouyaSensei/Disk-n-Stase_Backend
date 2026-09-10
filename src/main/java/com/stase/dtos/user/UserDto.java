package com.stase.dtos.user;

import com.stase.dtos.librairy.LibrairyDto;

// sortie du CRUD : le mot de passe (hash) n'est jamais expose au client
public record UserDto(
    Long id,
    String username,
    String email,
    LibrairyDto librairy
) {}
