// src/main/java/com/example/fitness_club/controller/admin/AdminTrainerController.java
package com.example.fitness_club.controller.admin;

import com.example.fitness_club.model.Trainer;
import com.example.fitness_club.repository.TrainerRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/trainers")
public class AdminTrainerController {

    private final TrainerRepository repo;

    public AdminTrainerController(TrainerRepository repo) {
        this.repo = repo;
    }

    // GET /api/admin/trainers
    @GetMapping
    public ResponseEntity<List<Trainer>> list() {
        return ResponseEntity.ok(repo.findAll());
    }

    // POST /api/admin/trainers
    @PostMapping
    public ResponseEntity<Trainer> create(@Valid @RequestBody Trainer t) {
        Trainer saved = repo.save(t);
        return ResponseEntity.ok(saved);
    }

    // PUT /api/admin/trainers/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @Valid @RequestBody Trainer t) {
        return repo.findById(id)
                .map(existing -> {
                    existing.setFirstName(t.getFirstName());
                    existing.setLastName(t.getLastName());
                    existing.setSpecialization(t.getSpecialization());
                    Trainer updated = repo.save(existing);
                    return ResponseEntity.ok(updated);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /api/admin/trainers/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
