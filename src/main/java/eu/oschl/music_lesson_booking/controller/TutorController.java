package eu.oschl.music_lesson_booking.controller;

import eu.oschl.music_lesson_booking.database.entity.Instrument;
import eu.oschl.music_lesson_booking.database.entity.Tutor;
import eu.oschl.music_lesson_booking.database.entity.TutorUnavailability;
import eu.oschl.music_lesson_booking.dto.InstrumentRequest;
import eu.oschl.music_lesson_booking.dto.TutorRequest;
import eu.oschl.music_lesson_booking.dto.TutorUnavailabilityRequest;
import eu.oschl.music_lesson_booking.dto.TutorVerificationRequest;
import eu.oschl.music_lesson_booking.dto.TutorVerificationResponse;
import eu.oschl.music_lesson_booking.service.AuthService;
import eu.oschl.music_lesson_booking.service.TutorService;
import java.net.URI;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin
@RequestMapping("/tutors")
public class TutorController {

    private AuthService authService;
    private TutorService tutorService;

    public TutorController(AuthService authService, TutorService tutorService) {
        this.authService = authService;
        this.tutorService = tutorService;
    }

    @GetMapping("/{id}")
    public Tutor getTutor(@PathVariable Long id) {
        return tutorService.getTutorById(id);
    }

    @PostMapping("/verify")
    public ResponseEntity<TutorVerificationResponse> verifyTutor(
        @RequestBody TutorVerificationRequest body
    ) {
        var tutor = authService.verifyTutorLogin(
            body.username(),
            body.password()
        );

        if (tutor != null) {
            return ResponseEntity.ok(
                new TutorVerificationResponse(
                    true,
                    tutor.getId(),
                    tutor.getUsername(),
                    tutor.getFirstName(),
                    tutor.getLastName()
                )
            );
        }

        return ResponseEntity.ok(
            new TutorVerificationResponse(false, null, null, null, null)
        );
    }

    @PostMapping("/")
    public ResponseEntity<Tutor> createTutor(@RequestBody TutorRequest body) {
        var tutor = this.tutorService.createTutor(body);

        return ResponseEntity.created(
            URI.create("/tutors/" + tutor.getId())
        ).body(tutor);
    }

    @GetMapping("/eligible")
    public List<Tutor> getEligibleTutors(
        @RequestParam int pupilMatric,
        @RequestParam(required = false) String instrument
    ) {
        return tutorService.getEligibleTutors(pupilMatric, instrument);
    }

    @GetMapping("/unavailabilities/{id}")
    public TutorUnavailability getTutorUnavailability(@PathVariable Long id) {
        return tutorService.getUnavailabiltyById(id);
    }

    @GetMapping("/unavailabilities/upcoming")
    public List<TutorUnavailability> getUpcomingUnavailabilities(
        @RequestParam Long tutorId
    ) {
        return tutorService.getUpcomingTutorUnavailabilities(tutorId);
    }

    @PostMapping("/unavailabilities")
    public ResponseEntity<TutorUnavailability> createTutorUnavailability(
        @RequestHeader(value = "Authorization", required = false) String auth,
        @RequestBody TutorUnavailabilityRequest body
    ) {
        if (auth == null) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Missing authorization header"
            );
        }

        var authorizedTutor = verifyAuthorization(auth);

        if (
            authorizedTutor == null ||
            !Objects.equals(authorizedTutor.getId(), body.tutorId())
        ) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "Forbidden"
            );
        }

        var tutorUnavailability = this.tutorService.createTutorUnavailability(
            body
        );

        return ResponseEntity.created(
            URI.create("/tutors/unavailabilities/" + tutorUnavailability.getId())
        ).body(tutorUnavailability);
    }

    @DeleteMapping("/unavailabilities/{id}")
    public ResponseEntity<Void> deleteTutorUnavailability(
        @RequestHeader(value = "Authorization", required = false) String auth,
        @PathVariable Long id
    ) {
        if (auth == null) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Missing authorization header"
            );
        }

        var authorizedTutor = verifyAuthorization(auth);

        if (authorizedTutor == null) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Unauthorized"
            );
        }

        var unavailability = this.tutorService.getUnavailabiltyById(id);

        if (
            !Objects.equals(
                unavailability.getTutor().getId(),
                authorizedTutor.getId()
            )
        ) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "Forbidden"
            );
        }

        tutorService.deleteTutorUnavailability(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/instruments/{id}")
    public Instrument getInstrument(@PathVariable Long id) {
        return tutorService.getInstrumentById(id);
    }

    @PostMapping("/instruments")
    public ResponseEntity<Instrument> createInstrument(
        @RequestHeader(value = "Authorization", required = false) String auth,
        @RequestBody InstrumentRequest body
    ) {
        if (auth == null) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Missing authorization header"
            );
        }

        var authorizedTutor = verifyAuthorization(auth);

        if (
            authorizedTutor == null ||
            !Objects.equals(authorizedTutor.getId(), body.tutorId())
        ) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "Forbidden"
            );
        }

        var instrument = this.tutorService.createInstrument(body);

        return ResponseEntity.created(
            URI.create("/tutors/instruments/" + instrument.getId())
        ).body(instrument);
    }

    @DeleteMapping("/instruments/{id}")
    public ResponseEntity<Void> deleteInstrument(
        @RequestHeader(value = "Authorization", required = false) String auth,
        @PathVariable Long id
    ) {
        if (auth == null) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Missing authorization header"
            );
        }

        var authorizedTutor = verifyAuthorization(auth);

        if (authorizedTutor == null) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Unauthorized"
            );
        }

        var instrument = this.tutorService.getInstrumentById(id);

        if (
            !Objects.equals(
                instrument.getTutor().getId(),
                authorizedTutor.getId()
            )
        ) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "Forbidden"
            );
        }

        tutorService.deleteInstrument(id);

        return ResponseEntity.noContent().build();
    }

    private Tutor verifyAuthorization(String auth) {
        String authString = new String(
            Base64.getDecoder().decode(auth.substring(6))
        );

        String username = authString.substring(0, authString.indexOf(":"));
        String password = authString.substring(authString.indexOf(":") + 1);

        return authService.verifyTutorLogin(username, password);
    }
}
