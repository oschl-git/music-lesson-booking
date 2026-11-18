package eu.oschl.music_lesson_booking.dto;

import java.util.Map;

public record Pupil(
    int age,
    Map<String, InstrumentGrade> instruments,
    int matric,
    String name
) {}
