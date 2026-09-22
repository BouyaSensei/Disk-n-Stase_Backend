package com.stase.repositories;

import com.stase.entities.Check_physique;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameFilterRepository
    extends JpaRepository<Check_physique, Long> {}
