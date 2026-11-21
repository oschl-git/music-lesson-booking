package eu.oschl.music_lesson_booking.database.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
public class Tutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @OneToMany(mappedBy = "tutor")
    private List<Instrument> instruments;

    @OneToMany(mappedBy = "tutor")
    private List<TutorUnavailability> tutorUnavailabilities;

    protected Tutor() {}

    public Tutor(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<Instrument> getInstruments() {
        return instruments;
    }

    public List<TutorUnavailability> getTutorUnavailabilities() {
        return tutorUnavailabilities;
    }
}
