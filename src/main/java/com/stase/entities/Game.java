package com.stase.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Jeu")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    private String description;
    private List<String> genre;
    private Boolean isPhysical;
    @ElementCollection // Indispensable pour une liste de types simples (String, Integer, etc.)
    @CollectionTable(name = "game_languages", joinColumns = @JoinColumn(name = "game_id"))
    @Column(name = "language")
    private List<String> languages;
    private String coverImageUrl;
    private String platforms;

    public Game() {
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "library_game", joinColumns = @JoinColumn(name = "game_id"), inverseJoinColumns = @JoinColumn(name = "librairy_id"))
    private final List<LibrairyEntry> libraries = new ArrayList<>();

    public Game(String name, String description, List<String> genre, Boolean isPhysical, List<String> languages,

            String coverImageUrl, String platforms) {
        this.name = name;
        this.description = description;
        this.genre = genre;
        this.isPhysical = isPhysical;
        this.languages = languages;
        this.coverImageUrl = coverImageUrl;
        this.platforms = platforms;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

    public Boolean getIsPhysical() {
        return isPhysical;
    }

    public void setIsPhysical(Boolean isPhysical) {
        this.isPhysical = isPhysical;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;

    }

    public List<LibrairyEntry> getLibrairy() {
        return libraries;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public String getPlatforms() {
        return platforms;
    }

    public void setPlatforms(String platforms) {
        this.platforms = platforms;
    }
}
