package eu.oschl.music_lesson_booking.dto;

import java.time.LocalDateTime;

public record BookingRequest(
    Long tutorId,
    int pupilMatric,
    String pupilName,
    String instrument,
    LocalDateTime startDateTime
) {}
