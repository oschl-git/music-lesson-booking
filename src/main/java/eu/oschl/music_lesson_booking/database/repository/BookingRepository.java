package eu.oschl.music_lesson_booking.database.repository;

import eu.oschl.music_lesson_booking.database.entity.Booking;
import eu.oschl.music_lesson_booking.database.entity.Tutor;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    boolean existsByTutorAndStartDateTime(
        Tutor tutor,
        LocalDateTime startDateTime
    );

    boolean existsByPupilMatricAndStartDateTimeBetween(
        int pupilMatric,
        LocalDateTime start,
        LocalDateTime end
    );
}
