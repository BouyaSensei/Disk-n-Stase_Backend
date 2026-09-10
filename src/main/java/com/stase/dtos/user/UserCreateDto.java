package com.stase.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// entree du CRUD : chaque champ est valide avant d'atteindre la couche service,
// ce qui bloque les payloads malformes et les caracteres hostiles (injection / XSS stocke)
public record UserCreateDto(
    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    @Size(min = 3, max = 64, message = "Le nom d'utilisateur doit faire entre 3 et 64 caracteres")
    @Pattern(
        regexp = "^[a-zA-Z0-9._-]+$",
        message = "Le nom d'utilisateur ne peut contenir que des lettres, chiffres, points, tirets et underscores"
    )
    String username,

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, max = 72, message = "Le mot de passe doit faire entre 8 et 72 caracteres")
    String password,

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email n'est pas valide")
    @Size(max = 255, message = "L'email est trop long")
    String email
) {
}
