package eu.oschl.music_lesson_booking.service;

import eu.oschl.music_lesson_booking.database.entity.Tutor;
import eu.oschl.music_lesson_booking.database.repository.InstrumentRepository;
import eu.oschl.music_lesson_booking.database.repository.TutorRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

@Service
public class TutorService {

    private final TutorRepository tutors;
    private final InstrumentRepository instruments;

    public TutorService(
        TutorRepository tutors,
        InstrumentRepository instruments
    ) {
        this.tutors = tutors;
        this.instruments = instruments;
    }

    public Tutor getTutorById(Long id) {
        return tutors.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
