package eu.oschl.music_lesson_booking.service;

import eu.oschl.music_lesson_booking.database.entity.Instrument;
import eu.oschl.music_lesson_booking.database.entity.Tutor;
import eu.oschl.music_lesson_booking.database.entity.TutorUnavailability;
import eu.oschl.music_lesson_booking.database.repository.BookingRepository;
import eu.oschl.music_lesson_booking.database.repository.InstrumentRepository;
import eu.oschl.music_lesson_booking.database.repository.TutorRepository;
import eu.oschl.music_lesson_booking.database.repository.TutorUnavailabilityRepository;
import eu.oschl.music_lesson_booking.dto.InstrumentRequest;
import eu.oschl.music_lesson_booking.dto.Pupil;
import eu.oschl.music_lesson_booking.dto.TutorRequest;
import eu.oschl.music_lesson_booking.dto.TutorUnavailabilityRequest;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TutorService {

    private final AuthService authService;
    private final BookingRepository bookings;
    private final InstrumentRepository instruments;
    private final PupilService pupilService;
    private final TutorRepository tutors;
    private final TutorUnavailabilityRepository tutorUnavailabilities;

    public TutorService(
        TutorRepository tutors,
        BookingRepository bookings,
        PupilService pupilService,
        TutorUnavailabilityRepository tutorUnavailabilities,
        AuthService authService,
        InstrumentRepository instruments
    ) {
        this.tutors = tutors;
        this.bookings = bookings;
        this.pupilService = pupilService;
        this.tutorUnavailabilities = tutorUnavailabilities;
        this.authService = authService;
        this.instruments = instruments;
    }

    public Tutor createTutor(TutorRequest request) {
        var passwordHash = this.authService.generateDatabasePasswordString(
            request.password()
        );

        var tutor = new Tutor(
            request.username(),
            passwordHash,
            request.firstName(),
            request.lastName()
        );

        tutor = this.tutors.save(tutor);

        return tutor;
    }

    public Tutor getTutorById(Long id) {
        return tutors.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Tutor getTutorByUsername(String username) {
        var tutor = tutors.findOneByUsername(username);

        if (tutor == null) {
            throw new EntityNotFoundException();
        }

        return tutor;
    }

    public List<Tutor> getEligibleTutors(int pupilMatric, String instrument) {
        var eligibleTutors = new ArrayList<Tutor>();

        var tutors = this.tutors.findAll();
        var pupil = this.pupilService.getPupilByMatric(pupilMatric);

        for (Tutor tutor : tutors) {
            for (String pupilInstrument : pupil.instruments().keySet()) {
                if (instrument != null && !instrument.equals(pupilInstrument)) {
                    continue;
                }

                if (
                    canTutorTeachPupilInstrument(pupilInstrument, tutor, pupil)
                ) {
                    eligibleTutors.add(tutor);
                    break;
                }
            }
        }

        return eligibleTutors;
    }

    public boolean canTutorTeachPupilInstrument(
        String instrument,
        Tutor tutor,
        Pupil pupil
    ) {
        var pupilInstrument = pupil.instruments().get(instrument);

        if (pupilInstrument == null) {
            return false;
        }

        Instrument tutorInstrument = null;
        for (var singleInstrument : tutor.getInstruments()) {
            if (singleInstrument.getInstrument().equals(instrument)) {
                tutorInstrument = singleInstrument;
                break;
            }
        }

        if (tutorInstrument == null) {
            return false;
        }

        if (
            pupilInstrument.grade() < tutorInstrument.getMinLevel() ||
            pupilInstrument.grade() > tutorInstrument.getMaxLevel()
        ) {
            return false;
        }

        return true;
    }

    public boolean isTutorAvailableInTimeslot(
        LocalDateTime startDateTime,
        Tutor tutor
    ) {
        var tutorUnavailabilities = tutor.getTutorUnavailabilities();

        for (TutorUnavailability tutorUnavailability : tutorUnavailabilities) {
            if (
                startDateTime
                    .plusMinutes(30)
                    .isAfter(tutorUnavailability.getStartTime()) &&
                startDateTime.isBefore(tutorUnavailability.getEndTime())
            ) {
                return false;
            }
        }

        if (bookings.existsByTutorAndStartDateTime(tutor, startDateTime)) {
            return false;
        }

        return true;
    }

    public List<TutorUnavailability> getUpcomingTutorUnavailabilities(
        Long tutorId
    ) {
        return this.tutorUnavailabilities.findUpcomingUnavailabilities(
            tutorId,
            LocalDateTime.now()
        );
    }

    public TutorUnavailability getUnavailabiltyById(Long unavailabilityId) {
        return this.tutorUnavailabilities
            .findById(unavailabilityId)
            .orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND)
            );
    }

    public TutorUnavailability createTutorUnavailability(
        TutorUnavailabilityRequest body
    ) {
        Tutor tutor;

        try {
            tutor = this.getTutorById(body.tutorId());
        } catch (EntityNotFoundException exception) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Tutor with the provided ID does not exist"
            );
        }

        if (!body.startTime().isAfter(body.endTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start time of the unavailability must be before the end time")
        }

        var tutorUnavailability = new TutorUnavailability(
            tutor,
            body.startTime(),
            body.endTime()
        );

        tutorUnavailability = tutorUnavailabilities.save(tutorUnavailability);

        return tutorUnavailability;
    }

    public void deleteTutorUnavailability(Long tutorUnavailabilityId) {
        this.tutorUnavailabilities.deleteById(tutorUnavailabilityId);
    }

    public Instrument getInstrumentById(Long instrumentId) {
        return this.instruments
            .findById(instrumentId)
            .orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND)
            );
    }

    public Instrument createInstrument(
        InstrumentRequest body
    ) {
        Tutor tutor;

        try {
            tutor = this.getTutorById(body.tutorId());
        } catch (EntityNotFoundException exception) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Tutor with the provided ID does not exist"
            );
        }

        var instrument = new Instrument(
            tutor,
            body.instrument(),
            body.minLevel(),
            body.maxLevel()
        );

        instrument = this.instruments.save(instrument);

        return instrument;
    }

    public void deleteInstrument(Long instrumentId) {
        this.instruments.deleteById(instrumentId);
    }
}
