package com.example.fitness_club.controller;

import com.example.fitness_club.model.Booking;
import com.example.fitness_club.model.Session;
import com.example.fitness_club.model.User;
import com.example.fitness_club.repository.BookingRepository;
import com.example.fitness_club.repository.SessionRepository;
import com.example.fitness_club.repository.UserRepository;
import jakarta.validation.Valid;
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

    public BookingController(BookingRepository bookingRepository,
                             UserRepository userRepository,
                             SessionRepository sessionRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    // GET /api/bookings
    @GetMapping
    public ResponseEntity<List<Booking>> listAll() {
        return ResponseEntity.ok(bookingRepository.findAll());
    }

    // GET /api/bookings/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> listByUser(@PathVariable Long userId) {
        Optional<User> u = userRepository.findById(userId);
        if (u.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("Пользователь с id=" + userId + " не найден");
        }
        return ResponseEntity.ok(bookingRepository.findByUserId(userId));
    }

    // POST /api/bookings
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Booking booking) {
        // 1) Проверяем пользователя
        Optional<User> u = userRepository.findById(booking.getUser().getId());
        if (u.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("Пользователь с id=" + booking.getUser().getId() + " не найден");
        }

        // 2) Проверяем сессию
        Optional<Session> s = sessionRepository.findById(booking.getSession().getId());
        if (s.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("Сессия с id=" + booking.getSession().getId() + " не найдена");
        }
        Session session = s.get();

        // 3) Проверяем, что у сессии задан capacity
        Integer cap = session.getCapacity();
        if (cap == null) {
            return ResponseEntity
                    .badRequest()
                    .body("У сессии с id=" + session.getId() + " не задано поле capacity");
        }

        // 4) Проверяем, не переполнена ли сессия
        long alreadyBooked = bookingRepository.countBySessionId(session.getId());
        if (alreadyBooked >= cap) {
            return ResponseEntity
                    .badRequest()
                    .body("Невозможно забронировать: сессия заполнена (capacity=" + cap + ")");
        }

        // 5) Всё ок — создаём бронь
        Booking b = new Booking(u.get(), session);
        Booking saved = bookingRepository.save(b);
        return ResponseEntity.ok(saved);
    }

    // DELETE /api/bookings/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancel(@PathVariable Long id) {
        if (!bookingRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        bookingRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
