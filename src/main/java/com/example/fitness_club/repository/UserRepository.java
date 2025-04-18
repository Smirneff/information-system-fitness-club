package com.example.fitness_club.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.fitness_club.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
