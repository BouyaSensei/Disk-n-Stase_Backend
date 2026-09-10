package com.stase.repositories;

import com.stase.entities.LibrairyEntry;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LibrairyRepository extends JpaRepository<LibrairyEntry, Long> {
    @Query("select le from LibrairyEntry le where le.user.id = :userId")
    Optional<LibrairyEntry> findByUserId(@Param("userId") Long userId);

    // User.java n'expose pas d'accesseur sur librairyEntry : on met a jour la FK cote Utilisateur
    @Modifying(clearAutomatically = true)
    @Query("update User u set u.librairyEntry = :entry where u.id = :userId")
    int assignToUser(
        @Param("userId") Long userId,
        @Param("entry") LibrairyEntry entry
    );
}
