package com.stase.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Bibliotheque")
public class LibrairyEntry {
    public LibrairyEntry() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "librairyEntry")
    private User user;

    @ManyToMany(mappedBy = "libraries", fetch = FetchType.LAZY)
    private List<Game> games = new ArrayList<>();
}
