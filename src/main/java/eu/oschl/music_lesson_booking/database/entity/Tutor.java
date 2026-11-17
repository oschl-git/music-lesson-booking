package eu.oschl.music_lesson_booking.database.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Tutor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String lastName;

    @OneToMany(mappedBy = "tutor")
    private List<Instrument> instruments;

    protected Tutor() {}

    public Tutor(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
