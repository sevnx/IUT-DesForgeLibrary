package application.server.domain;

import application.server.domain.core.Abonne;
import application.server.domain.core.Entity;
import application.server.domain.core.SimpleEntity;
import application.server.models.SubscriberModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

public class SimpleAbonne extends SimpleEntity<SubscriberModel> implements Abonne {
    private int id;
    private String firstName;
    private String lastName;
    private LocalDate birthdate;
    private boolean banned;

    public SimpleAbonne(int id, String firstName, String lastName, LocalDate birthdate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.banned = false;
    }

    public SimpleAbonne() {
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

        return this;
    }


    @Override
    public void save() throws SQLException {
        synchronized (this) {
            new SubscriberModel().save(this);
        }
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
