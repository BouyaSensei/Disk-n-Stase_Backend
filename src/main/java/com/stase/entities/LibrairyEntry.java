package com.stase.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class LibrairyEntry {
    public LibrairyEntry(){

    }
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "user")
    private User user;

    @ManyToMany(mappedBy="libraries",fetch = FetchType.LAZY)
    private List<Game> games = new ArrayList<>();
}
