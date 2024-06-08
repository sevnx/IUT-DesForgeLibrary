package application.server.entities;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Represents a subscriber of the application.
 */
public interface Abonne {
    int getId();

    int getAge();

    String getNom();

    boolean isBanned();

    void banUser();

    void unbanUser();

    void setBannedUntil(LocalDateTime bannedUntil);

    Optional<LocalDateTime> bannedUntil();

    String getEmail();

    void save() throws SQLException;
}
