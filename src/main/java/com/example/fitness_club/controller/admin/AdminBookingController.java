// src/main/java/com/example/fitness_club/controller/admin/AdminBookingController.java
package com.example.fitness_club.controller.admin;

import com.example.fitness_club.model.Booking;
import com.example.fitness_club.repository.BookingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/bookings")
public class AdminBookingController {

    private final BookingRepository bookingRepo;

    public AdminBookingController(BookingRepository bookingRepo) {
        this.bookingRepo = bookingRepo;
    }

    // GET /api/admin/bookings
    @GetMapping
    public ResponseEntity<List<Booking>> listAll() {
        return ResponseEntity.ok(bookingRepo.findAll());
    }

    // GET /api/admin/bookings/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return bookingRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/admin/bookings/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!bookingRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        bookingRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
