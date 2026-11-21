package eu.oschl.music_lesson_booking.database.repository;

import eu.oschl.music_lesson_booking.database.entity.Booking;
import eu.oschl.music_lesson_booking.database.entity.Tutor;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query(
        """
        SELECT b FROM Booking b
        WHERE b.pupilMatric = :matric
        AND b.startDateTime >= :start
        AND b.startDateTime < :end
        ORDER BY b.startDateTime
        """
    )
    List<Booking> findBookingsForPupilInRange(
        @Param("matric") int matric,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );

    @Query(
        """
        SELECT b FROM Booking b
        WHERE b.tutor.id = :tutorId
        AND b.startDateTime >= :start
        AND b.startDateTime < :end
        ORDER BY b.startDateTime
        """
    )
    List<Booking> findBookingsForTutorInRange(
        @Param("tutorId") Long tutorId,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );
}
