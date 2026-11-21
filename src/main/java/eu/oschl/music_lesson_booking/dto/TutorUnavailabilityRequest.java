package eu.oschl.music_lesson_booking.dto;

import java.time.LocalDateTime;

public record TutorUnavailabilityRequest(
    Long tutorId,
    LocalDateTime startTime,
    LocalDateTime endTime
) {}
