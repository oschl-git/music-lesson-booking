package eu.oschl.music_lesson_booking.database.repository;

import eu.oschl.music_lesson_booking.database.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {}
