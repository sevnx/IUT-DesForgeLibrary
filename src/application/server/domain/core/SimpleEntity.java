package application.server.domain.core;

import application.server.models.Model;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class SimpleEntity<T extends Model<?>> implements Entity<T> {
    public abstract int getId();

    public abstract Entity<T> mapEntity(ResultSet resultSet) throws SQLException;

    public String getIdentifier() {
        return getEntityName() + "-" + getId();
    }

    /**
     * Get the name of the entity.
     * @return Name of the entity
     */
    public abstract String getEntityName();

    public abstract void save() throws SQLException;
}
