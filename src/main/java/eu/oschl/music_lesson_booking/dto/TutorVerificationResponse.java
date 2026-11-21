package eu.oschl.music_lesson_booking.dto;

public record TutorVerificationResponse(
    boolean valid,
    Long id,
    String username,
    String firstName,
    String lastName
) {}
