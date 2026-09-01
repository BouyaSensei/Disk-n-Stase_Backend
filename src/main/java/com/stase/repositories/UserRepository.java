package com.stase.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stase.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
