package eu.oschl.music_lesson_booking.controller;

import eu.oschl.music_lesson_booking.database.entity.Tutor;
import eu.oschl.music_lesson_booking.database.entity.TutorUnavailability;
import eu.oschl.music_lesson_booking.dto.TutorRequest;
import eu.oschl.music_lesson_booking.dto.TutorVerificationRequest;
import eu.oschl.music_lesson_booking.dto.TutorVerificationResponse;
import eu.oschl.music_lesson_booking.service.AuthService;
import eu.oschl.music_lesson_booking.service.TutorService;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/verify")
    public ResponseEntity<TutorVerificationResponse> verifyTutor(
        @RequestBody TutorVerificationRequest body
    ) {
        if (
            this.authService.verifyTutorLogin(body.username(), body.password())
        ) {
            var tutor = this.tutorService.getTutorByUsername(body.username());

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

    @PostMapping("/new")
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

    @GetMapping("/unavailabilities/upcoming")
    public List<TutorUnavailability> getUpcomingUnavailabilities(
        @RequestParam Long tutorId
    ) {
        return tutorService.getUpcomingTutorUnavailabilities(tutorId);
    }
}
