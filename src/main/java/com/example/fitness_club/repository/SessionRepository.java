package com.example.fitness_club.repository;

import com.example.fitness_club.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
}
