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


}
