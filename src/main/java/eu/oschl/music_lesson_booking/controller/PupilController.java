package eu.oschl.music_lesson_booking.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/pupils")
public class PupilController {
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return new ResponseEntity<String>("Hello", HttpStatus.OK);
    }
}
