package eu.oschl.music_lesson_booking.service;

import eu.oschl.music_lesson_booking.database.entity.Tutor;
import eu.oschl.music_lesson_booking.database.repository.TutorRepository;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final TutorRepository tutors;

    public AuthService(TutorRepository tutors) {
        this.tutors = tutors;
    }

    public String generateDatabasePasswordString(String plaintext) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        var passwordHash = hashPassword(plaintext, salt);

        return (
            HexFormat.of().formatHex(salt) +
            HexFormat.of().formatHex(passwordHash)
        );
    }

    public Tutor verifyTutorLogin(String username, String password) {
        var tutor = this.tutors.findOneByUsername(username);

        if (tutor == null) {
            return null;
        }

        byte[] storedSalt = HexFormat.of().parseHex(
            tutor.getPasswordHash().substring(0, 32)
        );

        byte[] storedHash = HexFormat.of().parseHex(
            tutor.getPasswordHash().substring(32)
        );

        byte[] newHash = hashPassword(password, storedSalt);

        if (MessageDigest.isEqual(storedHash, newHash)) {
            return tutor;
        } else {
            return null;
        }
    }

    private byte[] hashPassword(String plaintext, byte[] salt) {
        MessageDigest md;

        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException(
                "Hashing algorithm is not available"
            );
        }

        md.update(salt);

        return md.digest(plaintext.getBytes(StandardCharsets.UTF_8));
    }
}
