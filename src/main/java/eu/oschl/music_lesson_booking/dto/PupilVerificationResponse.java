package eu.oschl.music_lesson_booking.dto;

import java.util.Map;

public record PupilVerificationResponse(
    boolean valid,
    String name,
    Integer matric,
    Integer age,
    Map<String, InstrumentGrade> instruments
) {}
