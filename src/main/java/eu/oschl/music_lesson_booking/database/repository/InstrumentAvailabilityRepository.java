package eu.oschl.music_lesson_booking.database.repository;

import eu.oschl.music_lesson_booking.database.entity.InstrumentAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstrumentAvailabilityRepository extends JpaRepository<InstrumentAvailability, Long> {}
