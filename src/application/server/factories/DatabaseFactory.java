package application.server.factories;

import application.server.managers.ConfigurationManager;
import application.server.managers.DatabaseManager;

public class DatabaseFactory {
    public static void setupDatabase() {
        DatabaseManager.setFromConfig(ConfigurationManager.getDatabaseConfig());
    }
}
