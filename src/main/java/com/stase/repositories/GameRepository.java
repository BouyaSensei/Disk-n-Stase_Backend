package com.stase.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stase.entities.Game;

public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findByRawgId(Long rawgId);

}
