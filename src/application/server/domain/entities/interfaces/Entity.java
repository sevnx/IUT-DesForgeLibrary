package application.server.domain.entities.interfaces;

import application.server.models.Model;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Entity<T extends Model<?>> {
    int getId();

    Entity<T> mapEntity(ResultSet resultSet) throws SQLException;

    String getIdentifier();

    void save() throws SQLException;
}
