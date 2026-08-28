package com.stase.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stase.entities.LibrairyEntry;

public interface LibrairyRepository extends JpaRepository<LibrairyEntry, Long> {

}
