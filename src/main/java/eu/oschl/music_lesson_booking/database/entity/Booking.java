package eu.oschl.music_lesson_booking.database.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tutor_id", nullable = false)
    private Tutor tutor;

    private String instrument;
    private int pupilMatric;
    private LocalDateTime startDateTime;

    protected Booking() {}

    public Booking(
        Tutor tutor,
        String instrument,
        int pupilMatric,
        LocalDateTime startDateTime
    ) {
        this.tutor = tutor;
        this.instrument = instrument;
        this.pupilMatric = pupilMatric;
        this.startDateTime = startDateTime;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public String getInstrument() {
        return instrument;
    }

    public int getPupilMatric() {
        return pupilMatric;
    }
}
