package eu.oschl.music_lesson_booking.service;

import eu.oschl.music_lesson_booking.database.entity.Tutor;
import eu.oschl.music_lesson_booking.database.repository.InstrumentRepository;
import eu.oschl.music_lesson_booking.database.repository.TutorRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TutorService {

    private final TutorRepository repository;
    private final InstrumentRepository instrumentRepository;

    public TutorService(
        TutorRepository repository,
        InstrumentRepository instrumentRepository
    ) {
        this.repository = repository;
        this.instrumentRepository = instrumentRepository;
    }

    public List<Tutor> getAllTutors() {
        return this.repository.findAll();
    }
}
