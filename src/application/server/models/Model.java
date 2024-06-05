package application.server.models;

import application.server.domain.core.Entity;
import application.server.managers.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Vector;

/**
 * Generic model class for database operations.
 * Only works with database tables that have an id column as primary key.
 *
 * @param <T> Mapped entity
 */
public abstract class Model<T extends Entity<?>> {
    private final Connection connection;

    public Model() {
        this.connection = DatabaseManager.connect();
    }

    public abstract void save(T entity) throws SQLException;

    public String getFullTableName() {
        return getDatabaseName() + "." + getTableName();
    }

    public abstract String getTableName();

    public String getDatabaseName() {
        return DatabaseManager.getDatabaseName().orElseThrow(() -> new IllegalStateException("Database name not set"));
    }

    public abstract T getEntityInstance();

    public Vector<T> get() throws SQLException {
        try {
            ResultSet resultSet = entries();
            Vector<T> entities = new Vector<>();
            while (resultSet.next()) {
                entities.add(mapEntity(resultSet));
            }
            return entities;
        } catch (SQLException e) {
            throw new SQLException("Error while fetching entries from database", e);
        }
    }

    private T mapEntity(ResultSet resultSet) throws SQLException {
        T entity = getEntityInstance();
        return (T) entity.mapEntity(resultSet);
    }

    public T get(int id) throws SQLException {
        try {
            ResultSet resultSet = entry(id);
            resultSet.next();
            return mapEntity(resultSet);
        } catch (SQLException e) {
            throw new SQLException("Error while fetching entry from database", e);
        }
    }

    private ResultSet entries() throws SQLException {
        try {
            return connection
                    .createStatement()
                    .executeQuery("SELECT * FROM " + getFullTableName());
        } catch (SQLException e) {
            throw new SQLException("Error while fetching entries from database", e);
        }
    }

    private ResultSet entry(int id) throws SQLException {
        try {
            return connection
                    .createStatement()
                    .executeQuery("SELECT * FROM " + getFullTableName() + " WHERE id = " + id);
        } catch (SQLException e) {
            throw new SQLException("Error while fetching entry from database", e);
        }
    }

    public PreparedStatement prepareStatement(String query) throws SQLException {
        return connection.prepareStatement(query);
    }
}
