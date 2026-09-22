package com.stase.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "games_no_physical_media")
public class Check_physique {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String game_title;

    private String physical_status;

    //getter

    public Check_physique() {}

    public Long getId() {
        return id;
    }

    public String getGame_title() {
        return game_title;
    }

    public String getPhysical_status() {
        return physical_status;
    }

    //setter
    public void setGame_title(String title) {
        this.game_title = title;
    }

    public void setPhysical_status(String status) {
        this.physical_status = status;
    }
}
