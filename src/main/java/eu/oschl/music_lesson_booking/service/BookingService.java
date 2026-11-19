package eu.oschl.music_lesson_booking.service;

import eu.oschl.music_lesson_booking.database.entity.Booking;
import eu.oschl.music_lesson_booking.database.entity.Instrument;
import eu.oschl.music_lesson_booking.database.entity.Tutor;
import eu.oschl.music_lesson_booking.database.entity.TutorUnavailability;
import eu.oschl.music_lesson_booking.database.repository.BookingRepository;
import eu.oschl.music_lesson_booking.dto.BookingRequest;
import eu.oschl.music_lesson_booking.dto.Pupil;
import jakarta.persistence.EntityNotFoundException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BookingService {

    private final PupilService pupilService;
    private final TutorService tutorService;

    private final BookingRepository bookings;

    public BookingService(
        BookingRepository bookings,
        TutorService tutorService,
        PupilService pupilService
    ) {
        this.bookings = bookings;
        this.tutorService = tutorService;
        this.pupilService = pupilService;
    }

    public Booking createBooking(BookingRequest bookingRequest) {
        Tutor tutor;

        try {
            tutor = this.tutorService.getTutorById(bookingRequest.tutorId());
        } catch (EntityNotFoundException exception) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Tutor with the provided ID does not exist"
            );
        }

        var booking = new Booking(
            tutor,
            bookingRequest.instrument(),
            bookingRequest.pupilMatric(),
            bookingRequest.startDateTime()
        );

        try {
            if (!this.isBookingValid(booking, bookingRequest.pupilName())) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "The booking is not valid"
                );
            }
        } catch (NullPointerException exception) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "The request does not include all required fields"
            );
        }

        return this.bookings.save(booking);
    }

    public void cancelBooking(Long bookingId) {
        var booking = bookings.findById(bookingId).orElseThrow(
            () -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "The booking does not exist"
            )
        );

        if (!isNextWeek(booking.getStartDateTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only bookings next week can be cancelled");
        }

        this.bookings.deleteById(bookingId);
    }

    public List<Booking> getThisWeekBookingsForPupil(int matric) {
        var end = getNextMonday();
        var start = end.minusDays(7);

        return this.bookings.findBookingsForPupilInRange(matric, start.atStartOfDay(), end.atStartOfDay());
    }

    public List<Booking> getNextWeekBookingsForPupil(int matric) {
        var start = getNextMonday();
        var end = start.plusDays(7);

        return this.bookings.findBookingsForPupilInRange(matric, start.atStartOfDay(), end.atStartOfDay());
    }

    private boolean isBookingValid(Booking booking, String pupilName) {
        if (
            isOutsideAllowedTimeslots(
                booking.getStartDateTime().getDayOfWeek(),
                booking.getStartDateTime().toLocalTime()
            )
        ) {
            return false;
        }

        if (!isNextWeek(booking.getStartDateTime())) {
            return false;
        }

        var tutor = booking.getTutor();

        if (!tutorService.isTutorAvailableInTimeslot(booking.getStartDateTime(), tutor)) {
            return false;
        }

        var pupil = this.pupilService.getPupilByMatric(
            booking.getPupilMatric()
        );

        if (!pupil.name().equals(pupilName)) {
            return false;
        }

        if (
            pupil.age() < 16 &&
            booking
                .getStartDateTime()
                .toLocalTime()
                .isAfter(LocalTime.of(17, 30))
        ) {
            return false;
        }

        if (
            !tutorService.canTutorTeachPupilInstrument(
                booking.getInstrument(),
                tutor,
                pupil
            )
        ) {
            return false;
        }

        var date = booking.getStartDateTime().toLocalDate();

        if (
            this.bookings.existsByPupilMatricAndStartDateTimeBetween(
                pupil.matric(),
                date.atStartOfDay(),
                date.plusDays(1).atStartOfDay()
            )
        ) {
            return false;
        }

        return true;
    }

    private boolean isOutsideAllowedTimeslots(
        DayOfWeek dayOfWeek,
        LocalTime time
    ) {
        if (
            time.getMinute() % 30 != 0 ||
            time.getSecond() != 0 ||
            time.getNano() != 0
        ) {
            return true;
        }

        if (dayOfWeek == DayOfWeek.SUNDAY) {
            return true;
        }

        if (dayOfWeek == DayOfWeek.SATURDAY) {
            if (
                time.isBefore(LocalTime.of(9, 0)) ||
                time.isAfter(LocalTime.of(19, 30))
            ) {
                return true;
            }
        } else if (
            time.isBefore(LocalTime.of(16, 0)) ||
            time.isAfter(LocalTime.of(19, 30))
        ) {
            return true;
        }

        return false;
    }

    private boolean isNextWeek(LocalDateTime startDateTime) {
        var startDate = startDateTime.toLocalDate();

        var nextMonday = getNextMonday();
        var nextSunday = nextMonday.plusDays(6);

        if (
            startDate.isBefore(nextMonday) || startDate.isAfter(nextSunday)
        ) {
            return false;
        }

        return true;
    }

    private LocalDate getNextMonday() {
        var today = LocalDate.now();
        return today.plusDays(8 - today.getDayOfWeek().getValue());
    }
}
