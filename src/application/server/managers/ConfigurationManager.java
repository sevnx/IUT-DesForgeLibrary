package application.server.managers;

import application.server.configuration.DatabaseConfig;
import application.server.configuration.ServerConfig;
import application.server.configuration.TimerConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigurationManager {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static DatabaseConfig getDatabaseConfig() {
        try {
            String json = new String(Files.readAllBytes(Paths.get(DatabaseConfig.getDatabaseConfigFilePath())));
            return objectMapper.readValue(json, DatabaseConfig.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static TimerConfig getTimerConfig() {
        try {
            String json = new String(Files.readAllBytes(Paths.get(TimerConfig.getTimerConfigFilePath())));
            return objectMapper.readValue(json, TimerConfig.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ServerConfig getServerConfig() {
        try {
            String json = new String(Files.readAllBytes(Paths.get(ServerConfig.getServerConfigFilePath())));
            return objectMapper.readValue(json, ServerConfig.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
