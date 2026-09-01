package com.stase.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stase.entities.Game;

public interface GameRepository extends JpaRepository<Game, Long> {
}
