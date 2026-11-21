package eu.oschl.music_lesson_booking.controller;

import eu.oschl.music_lesson_booking.database.entity.Booking;
import eu.oschl.music_lesson_booking.dto.BookingRequest;
import eu.oschl.music_lesson_booking.service.BookingService;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/this-week/pupil")
    public List<Booking> getThisWeekBookingsForPupil(@RequestParam int matric) {
        return bookingService.getThisWeekBookingsForPupil(matric);
    }

    @GetMapping("/next-week/pupil")
    public List<Booking> getNextWeekBookingsForPupil(@RequestParam int matric) {
        return bookingService.getNextWeekBookingsForPupil(matric);
    }

    @GetMapping("/this-week/tutor")
    public List<Booking> getThisWeekBookingsForTutor(@RequestParam Long tutorId) {
        return bookingService.getThisWeekBookingsForTutor(tutorId);
    }

    @GetMapping("/next-week/tutor")
    public List<Booking> getNextWeekBookingsForTutor(@RequestParam Long tutorId) {
        return bookingService.getNextWeekBookingsForTutor(tutorId);
    }
}
