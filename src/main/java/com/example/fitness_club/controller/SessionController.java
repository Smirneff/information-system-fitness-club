package com.example.fitness_club.controller;

import com.example.fitness_club.model.Session;
import com.example.fitness_club.model.Trainer;
import com.example.fitness_club.repository.SessionRepository;
import com.example.fitness_club.repository.TrainerRepository;
import com.example.fitness_club.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionRepository sessionRepository;
    private final TrainerRepository trainerRepository;

    public SessionController(SessionRepository sessionRepository, TrainerRepository trainerRepository) {
        this.sessionRepository = sessionRepository;
        this.trainerRepository = trainerRepository;
    }

    @GetMapping
    public ResponseEntity<Iterable<Session>> getAll() {
        return ResponseEntity.ok(sessionRepository.findAll());
    }

}
