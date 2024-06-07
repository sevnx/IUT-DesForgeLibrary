package application.server.domain.entities.interfaces;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Subscriber interface
 */
public interface Abonne {
    int getId();

    int getAge();

    String getNom();

    boolean isBanned();

    void banUser();

    void unbanUser();

    Optional<LocalDateTime> bannedUntil();

    default String getEmail() {
        return "sewciooooo@gmail.com";
    }
}
