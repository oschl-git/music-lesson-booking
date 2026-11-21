package eu.oschl.music_lesson_booking.service;

import eu.oschl.music_lesson_booking.database.entity.Instrument;
import eu.oschl.music_lesson_booking.database.entity.Tutor;
import eu.oschl.music_lesson_booking.database.entity.TutorUnavailability;
import eu.oschl.music_lesson_booking.database.repository.BookingRepository;
import eu.oschl.music_lesson_booking.database.repository.InstrumentRepository;
import eu.oschl.music_lesson_booking.database.repository.TutorRepository;
import eu.oschl.music_lesson_booking.database.repository.TutorUnavailabilityRepository;
import eu.oschl.music_lesson_booking.dto.Pupil;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TutorService {

    private final TutorRepository tutors;
    private final InstrumentRepository instruments;
    private final BookingRepository bookings;
    private final PupilService pupilService;
    private final TutorUnavailabilityRepository tutorUnavailabilities;

    public TutorService(
        TutorRepository tutors,
        InstrumentRepository instruments,
        BookingRepository bookings,
        PupilService pupilService,
        TutorUnavailabilityRepository tutorUnavailabilities
    ) {
        this.tutors = tutors;
        this.instruments = instruments;
        this.bookings = bookings;
        this.pupilService = pupilService;
        this.tutorUnavailabilities = tutorUnavailabilities;
    }

    public Tutor getTutorById(Long id) {
        return tutors.findById(id).orElseThrow(EntityNotFoundException::new);
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

    public List<TutorUnavailability> getUpcomingTutorUnavailabilities(Long tutorId) {
        return this.tutorUnavailabilities.findUpcomingUnavailabilities(tutorId, LocalDateTime.now());
    }
}
