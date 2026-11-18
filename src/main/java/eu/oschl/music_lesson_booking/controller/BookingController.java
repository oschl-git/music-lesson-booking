package eu.oschl.music_lesson_booking.controller;

import eu.oschl.music_lesson_booking.database.entity.Booking;
import eu.oschl.music_lesson_booking.database.entity.Tutor;
import eu.oschl.music_lesson_booking.dto.BookingRequest;
import eu.oschl.music_lesson_booking.service.BookingService;
import eu.oschl.music_lesson_booking.service.TutorService;
import jakarta.persistence.EntityNotFoundException;

import java.net.URI;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin
@RequestMapping("/bookings")
public class BookingController {

    private BookingService bookingService;
    private TutorService tutorService;

    public BookingController(BookingService bookingService, TutorService tutorService) {
        this.bookingService = bookingService;
        this.tutorService = tutorService;
    }

    @PostMapping("/new")
    public ResponseEntity<String> createBooking(
        @RequestBody BookingRequest body
    ) {
        var booking = this.bookingService.createBooking(body);

        return ResponseEntity.created(URI.create("/bookings/" + booking.getId())).build();
    }
}
