package com.stase.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stase.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
