package eu.oschl.music_lesson_booking.dto;

public record InstrumentRequest(
    Long tutorId,
    String instrument,
    int minLevel,
    int maxLevel
) {}
