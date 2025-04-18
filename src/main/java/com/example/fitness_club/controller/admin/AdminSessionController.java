// src/main/java/com/example/fitness_club/controller/admin/AdminSessionController.java
package com.example.fitness_club.controller.admin;

import com.example.fitness_club.model.Session;
import com.example.fitness_club.model.Trainer;
import com.example.fitness_club.repository.SessionRepository;
import com.example.fitness_club.repository.TrainerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/sessions")
public class AdminSessionController {

    private final SessionRepository sessionRepo;
    private final TrainerRepository trainerRepo;

    public AdminSessionController(SessionRepository sessionRepo,
                                  TrainerRepository trainerRepo) {
        this.sessionRepo = sessionRepo;
        this.trainerRepo = trainerRepo;
    }

    // GET /api/admin/sessions
    @GetMapping
    public ResponseEntity<List<Session>> listAll() {
        return ResponseEntity.ok(sessionRepo.findAll());
    }

    // GET /api/admin/sessions/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return sessionRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/admin/sessions
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Session session) {
        Long trainerId = session.getTrainer().getId();
        Optional<Trainer> t = trainerRepo.findById(trainerId);
        if (t.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Trainer with id=" + trainerId + " not found");
        }
        session.setTrainer(t.get());
        Session saved = sessionRepo.save(session);
        return ResponseEntity.ok(saved);
    }

    // PUT /api/admin/sessions/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody Session session) {
        return sessionRepo.findById(id)
                .map(existing -> {
                    existing.setTitle(session.getTitle());
                    existing.setStartTime(session.getStartTime());
                    existing.setEndTime(session.getEndTime());
                    Long trId = session.getTrainer().getId();
                    Optional<Trainer> tt = trainerRepo.findById(trId);
                    if (tt.isEmpty()) {
                        return ResponseEntity.badRequest()
                                .body("Trainer with id=" + trId + " not found");
                    }
                    existing.setTrainer(tt.get());
                    return ResponseEntity.ok(sessionRepo.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/admin/sessions/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!sessionRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        sessionRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
