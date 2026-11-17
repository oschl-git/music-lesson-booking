package eu.oschl.music_lesson_booking.service;

import eu.oschl.music_lesson_booking.database.entity.Tutor;
import eu.oschl.music_lesson_booking.database.repository.InstrumentAvailabilityRepository;
import eu.oschl.music_lesson_booking.database.repository.TutorRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TutorService {

    private final TutorRepository repository;
    private final InstrumentAvailabilityRepository instrumentAvailabilityRepository;

    public TutorService(
        TutorRepository repository,
        InstrumentAvailabilityRepository instrumentAvailabilityRepository
    ) {
        this.repository = repository;
        this.instrumentAvailabilityRepository = instrumentAvailabilityRepository;
    }

    public List<Tutor> getAllTutors() {
        return this.repository.findAll();
    }
}
