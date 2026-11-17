package eu.oschl.music_lesson_booking.database.repository;

import eu.oschl.music_lesson_booking.database.entity.TutorUnavailability;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TutorUnavailabilityRepository
    extends JpaRepository<TutorUnavailability, Long> {}
