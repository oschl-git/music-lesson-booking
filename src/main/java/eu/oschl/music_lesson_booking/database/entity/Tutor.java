package eu.oschl.music_lesson_booking.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Tutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
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

   	public String getUsername() {
		return username;
	}

	@JsonIgnore
	public String getPasswordHash() {
		return passwordHash;
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
