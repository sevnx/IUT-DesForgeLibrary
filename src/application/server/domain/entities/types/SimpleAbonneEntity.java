package application.server.domain.entities.types;

import application.server.domain.entities.interfaces.Abonne;
import application.server.domain.entities.interfaces.Entity;
import application.server.managers.TimerManager;
import application.server.models.SubscriberModel;
import application.server.timer.tasks.BanUserTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Optional;

public class SimpleAbonneEntity extends SimpleEntity<SubscriberModel> implements Abonne {
    private static final Logger LOGGER = LogManager.getLogger("Base Abonne Entity");
    private final Object banLock = new Object();
    private int id;
    private String firstName;
    private String lastName;
    private LocalDate birthdate;
    private boolean banned;
    private Optional<LocalDateTime> bannedUntil;

    public SimpleAbonneEntity() {
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Entity<SubscriberModel> mapEntity(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt("id");
        this.firstName = resultSet.getString("firstName");
        this.lastName = resultSet.getString("lastName");
        this.birthdate = resultSet.getDate("birthdate").toLocalDate();
        this.banned = resultSet.getBoolean("isBanned");

        Timestamp bannedUntil = resultSet.getTimestamp("bannedUntil");
        if (bannedUntil == null) {
            this.bannedUntil = Optional.empty();
        } else {
            this.bannedUntil = Optional.of(bannedUntil.toLocalDateTime());
        }

        return this;
    }

    public void banUser() {
        banned = true;
        bannedUntil = Optional.of(LocalDateTime.now().plusSeconds(BanUserTask.getDefaultDurationInSeconds()));
        BanUserTask banUserTask = new BanUserTask(this);
        TimerManager.startTimer(banUserTask.getTaskIdentifier(), banUserTask);

        try {
            this.save();
        } catch (SQLException e) {
            LOGGER.error("Failed to ban user", e);
            throw new RuntimeException("Failed to ban user", e);
        }
    }

    public void unbanUser() {
        synchronized (banLock) {
            banned = false;
            bannedUntil = Optional.empty();

            try {
                this.save();
            } catch (SQLException e) {
                LOGGER.error("Failed to unban user", e);
                throw new RuntimeException("Failed to unban user", e);
            }
        }
    }

    @Override
    public Optional<LocalDateTime> bannedUntil() {
        return bannedUntil;
    }

    @Override
    public void save() throws SQLException {
        synchronized (banLock) {
            new SubscriberModel().save(this);
        }
    }

    @Override
    public String getEntityName() {
        return "Abonne";
    }

    @Override
    public int getAge() {
        return Period.between(birthdate, LocalDate.now()).getYears();
    }

    @Override
    public String getNom() {
        return firstName + " " + lastName;
    }

    @Override
    public boolean isBanned() {
        return banned;
    }
}
