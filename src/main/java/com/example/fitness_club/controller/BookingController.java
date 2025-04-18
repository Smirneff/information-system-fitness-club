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

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Booking booking){
        Optional<User> u = userRepository.findById(booking.getUser().getId());
        if(u.isEmpty()){
            return ResponseEntity.badRequest().body("Пользователь с id= "+booking.getUser().getId()+ "не найден");
        }
        Optional<Session> s = sessionRepository.findById(booking.getSession().getId());
        if(s.isEmpty()){
            return ResponseEntity.badRequest()
                    .body("Сессия с id= " + booking.getSession().getId() + "не найдена");
        }

        Booking b = new Booking(u.get(), s.get());
        Booking saved = bookingRepository.save(b);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancel(@PathVariable Long id) {
        if (!bookingRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        bookingRepository.deleteById(id);
        return ResponseEntity.noContent().build();
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
