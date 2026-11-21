package eu.oschl.music_lesson_booking.database.repository;

import eu.oschl.music_lesson_booking.database.entity.TutorUnavailability;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TutorUnavailabilityRepository
    extends JpaRepository<TutorUnavailability, Long>
{
    @Query(
        """
        SELECT u FROM TutorUnavailability u
        WHERE u.tutor.id = :tutorId
        AND u.startTime >= :fromDate
        ORDER BY u.startTime
        """
    )
    List<TutorUnavailability> findUpcomingUnavailabilities(
        @Param("tutorId") Long tutorId,
        @Param("fromDate") LocalDateTime fromDate
    );
}
