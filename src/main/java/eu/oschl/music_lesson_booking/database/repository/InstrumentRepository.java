package eu.oschl.music_lesson_booking.database.repository;

import eu.oschl.music_lesson_booking.database.entity.Instrument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstrumentRepository extends JpaRepository<Instrument, Long> {}
