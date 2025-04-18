package com.example.fitness_club.controller;

import com.example.fitness_club.model.Booking;
import com.example.fitness_club.model.Session;
import com.example.fitness_club.model.User;
import com.example.fitness_club.repository.BookingRepository;
import com.example.fitness_club.repository.SessionRepository;
import com.example.fitness_club.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    public BookingController(BookingRepository bookingRepository, UserRepository userRepository, SessionRepository sessionRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    @GetMapping
    public ResponseEntity<List<Booking>> listAll(){
        return ResponseEntity.ok(bookingRepository.findAll());
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<?> listByUser(@PathVariable Long userId) {
        Optional<User> u = userRepository.findById(userId);
        if(u.isEmpty()){
            return ResponseEntity.badRequest().body("Пользователь с id= " + userId + "не найден");
        }
        List<Booking> bookings = bookingRepository.findByUserId(userId);
        return ResponseEntity.ok(bookings);
    }
}
