package application.server.domain.entities.types;

import application.server.domain.entities.interfaces.Abonne;
import application.server.domain.entities.interfaces.Entity;
import application.server.managers.TimerManager;
import application.server.models.SubscriberModel;
import application.server.timer.tasks.BanUserTask;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

public class SimpleAbonneEntity extends SimpleEntity<SubscriberModel> implements Abonne {
    private int id;
    private String firstName;
    private String lastName;
    private LocalDate birthdate;
    private boolean banned;

    public SimpleAbonneEntity() {}

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

        return this;
    }

    public void banUser() throws SQLException {
        banned = true;
        this.save();
        TimerManager.startTimer("TODO", new BanUserTask(this));
    }

public void unbanUser() {
        banned = false;
    }

    @Override
    public void save() throws SQLException {
        new SubscriberModel().save(this);
    }

    @Override
    public String getEntityName() {
        return "Abonne";
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    @Override
    public int getAge() {
        return Period.between(birthdate, LocalDate.now()).getYears();
    }

    @Override
    public boolean isBanned() {
        return banned;
    }
}
