package com.stase.entities;

import jakarta.persistence.*;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Game {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String title;
    private Text description;
    private String genre;
    private Boolean isPhysical;
    private List<String> languages;
    private String coverImageUrl;

    public Game() {
    }
    @ManyToMany
    @JoinTable(
            name="library_game",
            joinColumns = @JoinColumn(name="game_id"),
            inverseJoinColumns = @JoinColumn(name="librairy_id")
    )
    private final List<LibrairyEntry> libraries = new ArrayList<>();

    public Game(String title, Text description, String genre, Boolean isPhysical, List<String> languages,

                String coverImageUrl) {
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.isPhysical = isPhysical;
        this.languages = languages;
        this.coverImageUrl = coverImageUrl;

    }
    public Long getId() {
        return id;
    }
    public String getTitle(){
        return title;
    }
    public void setTitle(String title) {
         this.title = title;
    }
    public Text getDescription() {
        return description;
    }
    public void setDescription(Text description) {
        this.description = description;
    }
    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
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
    public List<LibrairyEntry>getLibrairy(){
        return libraries;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }
}
