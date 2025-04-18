// src/main/java/com/example/fitness_club/controller/admin/AdminTrainerController.java
package com.example.fitness_club.controller.admin;

import com.example.fitness_club.model.Trainer;
import com.example.fitness_club.repository.TrainerRepository;
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

    @GetMapping
    public ResponseEntity<List<Trainer>> list() {
        return ResponseEntity.ok(repo.findAll());
    }

    @PostMapping
    public ResponseEntity<Trainer> create(@RequestBody Trainer t) {
        return ResponseEntity.ok(repo.save(t));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody Trainer t) {
        return repo.findById(id)
                .map(ex -> {
                    ex.setFirstName(t.getFirstName());
                    ex.setLastName(t.getLastName());
                    ex.setSpecialization(t.getSpecialization());
                    return ResponseEntity.ok(repo.save(ex));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
