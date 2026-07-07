package com.stase.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class LibrairyEntry {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "user")
    private User user;

    @ManyToMany(mappedBy="librairies")
    private List<Game> games = new ArrayList<>();
}
