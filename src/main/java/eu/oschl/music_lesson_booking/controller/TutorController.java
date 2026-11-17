package eu.oschl.music_lesson_booking.controller;

import eu.oschl.music_lesson_booking.database.entity.Tutor;
import eu.oschl.music_lesson_booking.service.TutorService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/tutors")
public class TutorController {

    private final TutorService tutors;

    public TutorController(TutorService tutors) {
        this.tutors = tutors;
    }

    @GetMapping("/test")
    public ResponseEntity<List<Tutor>> test() {
        return new ResponseEntity<List<Tutor>>(
            this.tutors.getAllTutors(),
            HttpStatus.OK
        );
    }
}
