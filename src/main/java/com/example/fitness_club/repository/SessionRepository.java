package com.example.fitness_club.repository;

import com.example.fitness_club.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {
    // Сессии заданного тренера, которые начинаются до end и заканчиваются после start
    List<Session> findByTrainerIdAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
            Long trainerId, LocalDateTime end, LocalDateTime start);
}
