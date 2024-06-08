package application.server.models;

import application.server.entities.Entity;
import application.server.managers.DatabaseManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 * Generic model class for database operations.
 * Only works with database tables that have an id column as primary key.
 *
 * @param <T> Mapped entity
 */
public abstract class Model<T extends Entity<?>> {
    private static final Logger LOGGER = LogManager.getLogger("DB Model");

    private final Connection connection;

    public Model() {
        this.connection = DatabaseManager.connect();
    }

    /**
     * Saves the entity to the database.
     *
     * @param entity Entity to save
     * @throws SQLException If an error occurs while saving the entity
     */
    public abstract void save(T entity) throws SQLException;

    public String getFullTableName() {
        return getDatabaseName() + "." + getTableName();
    }

    public abstract String getTableName();

    public String getDatabaseName() {
        return DatabaseManager.getDatabaseName().orElseThrow(() -> new IllegalStateException("Database name not set"));
    }

    public abstract T getEntityInstance();

    /**
     * Fetches all entries from the database and maps them to entities.
     *
     * @return Vector of entities (thread safe)
     * @throws SQLException If an error occurs while fetching the entries
     */
    public Vector<T> get() throws SQLException {
        LOGGER.info("Fetching entries from database for {}", this.getClass().getSimpleName());
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

    /**
     * Maps the result set to an entity.
     *
     * @param resultSet Result set from a database query
     * @return Mapped entity
     * @throws SQLException If an error occurs while mapping the entity
     */
    private T mapEntity(ResultSet resultSet) throws SQLException {
        T entity = getEntityInstance();
        return (T) entity.mapEntity(resultSet);
    }

    /**
     * Fetches all entries from the database.
     *
     * @return Result set of entries
     * @throws SQLException If an error occurs while fetching the entries
     */
    private ResultSet entries() throws SQLException {
        try {
            return connection
                    .createStatement()
                    .executeQuery("SELECT * FROM " + getFullTableName());
        } catch (SQLException e) {
            throw new SQLException("Error while fetching entries from database", e);
        }
    }

    /**
     * Prepares a statement for execution.
     *
     * @param query Query to prepare
     * @return Prepared statement
     * @throws SQLException If an error occurs while preparing the statement
     */
    public PreparedStatement prepareStatement(String query) throws SQLException {
        return connection.prepareStatement(query);
    }
}
