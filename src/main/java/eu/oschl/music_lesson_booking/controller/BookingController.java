package eu.oschl.music_lesson_booking.controller;

import eu.oschl.music_lesson_booking.database.entity.Booking;
import eu.oschl.music_lesson_booking.dto.BookingRequest;
import eu.oschl.music_lesson_booking.service.BookingService;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/bookings")
public class BookingController {

    private BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/new")
    public ResponseEntity<Booking> createBooking(
        @RequestBody BookingRequest body
    ) {
        var booking = this.bookingService.createBooking(body);

        return ResponseEntity.created(
            URI.create("/bookings/" + booking.getId())
        ).body(booking);
    }
}
