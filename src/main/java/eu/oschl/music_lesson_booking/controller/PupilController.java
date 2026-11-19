package eu.oschl.music_lesson_booking.controller;

import eu.oschl.music_lesson_booking.dto.PupilVerificationRequest;
import eu.oschl.music_lesson_booking.dto.PupilVerificationResponse;
import eu.oschl.music_lesson_booking.service.PupilService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@CrossOrigin
@RequestMapping("/pupils")
public class PupilController {

    private PupilService pupilService;

    public PupilController(PupilService pupilService) {
        this.pupilService = pupilService;
    }

    @PostMapping("/verify")
    public ResponseEntity<PupilVerificationResponse> verifyPupil(
        @RequestBody PupilVerificationRequest body
    ) {
        try {
            var pupil = this.pupilService.getPupilByMatric(body.matric());

            if (
                pupil.matric() == body.matric() &&
                pupil.name().equals(body.name())
            ) {
                return ResponseEntity.ok(
                    new PupilVerificationResponse(
                        true,
                        pupil.name(),
                        pupil.matric(),
                        pupil.age(),
                        pupil.instruments()
                    )
                );
            }
        } catch (HttpClientErrorException.NotFound exception) {}

        return ResponseEntity.ok(
            new PupilVerificationResponse(false, null, null, null, null)
        );
    }
}
