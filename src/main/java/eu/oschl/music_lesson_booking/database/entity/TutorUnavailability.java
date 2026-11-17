package eu.oschl.music_lesson_booking.database.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class TutorUnavailability {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tutor_id", nullable = false)
    private Tutor tutor;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    protected TutorUnavailability() {}

    public TutorUnavailability(
        Tutor tutor,
        LocalDateTime startTime,
        LocalDateTime endTime
    ) {
        this.tutor = tutor;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
