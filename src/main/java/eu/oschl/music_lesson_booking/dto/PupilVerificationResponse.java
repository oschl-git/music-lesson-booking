package eu.oschl.music_lesson_booking.dto;

public record PupilVerificationResponse(
    boolean valid,
    String name,
    Integer matric,
    Integer age
) {}
