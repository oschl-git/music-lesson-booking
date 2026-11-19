package eu.oschl.music_lesson_booking.controller;

import eu.oschl.music_lesson_booking.database.entity.Tutor;
import eu.oschl.music_lesson_booking.service.TutorService;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/tutors")
public class TutorController {

    private TutorService tutorService;

    public TutorController(TutorService tutorService) {
        this.tutorService = tutorService;
    }

    @GetMapping("/eligible")
    public List<Tutor> getEligibleTutors(
        @RequestParam int pupilMatric,
        @RequestParam(required = false) String instrument
    ) {
        return tutorService.getEligibleTutors(pupilMatric, instrument);
    }
}
