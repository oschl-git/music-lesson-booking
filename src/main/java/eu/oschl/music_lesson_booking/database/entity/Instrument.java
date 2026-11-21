package eu.oschl.music_lesson_booking.database.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Instrument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tutor_id", nullable = false)
    private Tutor tutor;

    private String instrument;
    private int minLevel;
    private int maxLevel;

    protected Instrument() {}

    public Instrument(Tutor tutor, String instrument, int minLevel, int maxLevel) {
        this.tutor = tutor;
        this.instrument = instrument;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
    }

	public String getInstrument() {
		return instrument;
	}

	public int getMinLevel() {
		return minLevel;
	}

	public int getMaxLevel() {
		return maxLevel;
	}
}
