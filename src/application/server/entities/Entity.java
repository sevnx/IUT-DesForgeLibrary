package application.server.entities;

import application.server.models.Model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Runtime representation of a database entity.
 *
 * @param <T> The model associated with the entity (that we load from the database).
 */
public interface Entity<T extends Model<?>> {
    int getId();

    Entity<T> mapEntity(ResultSet resultSet) throws SQLException;

    void save() throws SQLException;
}
