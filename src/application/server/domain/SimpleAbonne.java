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
    private String name;
    private LocalDate birthday;
    private boolean banned;

    public SimpleAbonne(int id, String name, LocalDate birthday, boolean banned) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
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
        this.name = resultSet.getString("name");
        this.birthday = resultSet.getDate("birthday").toLocalDate();
        this.banned = resultSet.getBoolean("banned");

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
        return name;
    }

    @Override
    public int getAge() {
        return Period.between(birthday, LocalDate.now()).getYears();
    }

    @Override
    public boolean isBanned() {
        return banned;
    }
}
