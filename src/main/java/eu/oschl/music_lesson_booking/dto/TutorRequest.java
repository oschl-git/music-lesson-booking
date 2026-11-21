package eu.oschl.music_lesson_booking.dto;

public record TutorRequest(
    String username,
    String password,
    String firstName,
    String lastName
) {}
