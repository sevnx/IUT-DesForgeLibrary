package application.server.configuration;

import java.util.Objects;

public record DatabaseConfig(
        String databaseHost,
        int databasePort,
        String databaseUser,
        String databasePassword,
        String databaseName
) {
    public DatabaseConfig {
        Objects.requireNonNull(databaseHost, "The database host must not be null");
        Objects.requireNonNull(databaseName, "The database name must not be null");
    }

    private static String DATABASE_CONFIG_FILE_PATH = "config/database.json";

    public static String getDatabaseConfigFilePath() {
        return DATABASE_CONFIG_FILE_PATH;
    }
}
