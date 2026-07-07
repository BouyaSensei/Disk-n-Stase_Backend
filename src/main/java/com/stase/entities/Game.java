package com.stase.entities;

import jakarta.persistence.*;
import org.w3c.dom.Text;

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
    public Game(String title, Text description, String genre, Boolean isPhysical, List<String> languages, String coverImageUrl) {
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
}
