package com.stase.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// tous les champs sont optionnels : seuls les champs non null seront mis a jour
public record UserUpdateDto(
    @Size(min = 3, max = 64, message = "Le nom d'utilisateur doit faire entre 3 et 64 caracteres")
    @Pattern(
        regexp = "^[a-zA-Z0-9._-]+$",
        message = "Le nom d'utilisateur ne peut contenir que des lettres, chiffres, points, tirets et underscores"
    )
    String username,

    @Size(min = 8, max = 72, message = "Le mot de passe doit faire entre 8 et 72 caracteres")
    String password,

    @Email(message = "L'email n'est pas valide")
    @Size(max = 255, message = "L'email est trop long")
    String email
) {
}
