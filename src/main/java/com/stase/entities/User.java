package com.stase.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Utilisateur")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // unique cote base : dernier filet contre les doublons, meme en cas de concurrence
    @Column(nullable = false, unique = true, length = 64)
    private String username;

    // stocke le hash BCrypt du mot de passe, jamais le mot de passe en clair
    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    // chaque utilisateur possede sa propre librairie, attribuee personnellement a la creation
    @OneToOne
    @JoinColumn(name = "librairy_entry_id")
    private LibrairyEntry librairyEntry;

    public User() {}

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LibrairyEntry getLibrairyEntry() {
        return this.librairyEntry;
    }

    public void setLibrairyEntry(LibrairyEntry librairyEntry) {
        this.librairyEntry = librairyEntry;
    }
}
