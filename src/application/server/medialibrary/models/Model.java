package application.server.medialibrary.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 * Generic model class for database operations.
 * @param <T> Mapped entity class
 */
public abstract class Model<T> {
    private static final String DATABASE_NAME = "deslibrary";
    private final Connection connection;

    public Model(Connection connection) {
        this.connection = connection;
    }

    public abstract void save(T entity) throws SQLException;

    public abstract T mapEntity(ResultSet resultSet) throws SQLException;

    public String getFullTableName() {
        return getDatabaseName() + "." + getTableName();
    }

    public abstract String getTableName();

    public String getDatabaseName() {
        return DATABASE_NAME;
    }

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
                    .executeQuery("SELECT * FROM" + getFullTableName());
        } catch (SQLException e) {
            throw new SQLException("Error while fetching entries from database", e);
        }
    }

    private ResultSet entry(int id) throws SQLException {
        try {
            return connection
                    .createStatement()
                    .executeQuery("SELECT * FROM" + getFullTableName() + " WHERE id = " + id);
        } catch (SQLException e) {
            throw new SQLException("Error while fetching entry from database", e);
        }
    }

    public PreparedStatement prepareStatement(String query) throws SQLException {
        return connection.prepareStatement(query);
    }
}
