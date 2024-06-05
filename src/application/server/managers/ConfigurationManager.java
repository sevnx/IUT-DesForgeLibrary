package application.server.managers;

import application.server.configuration.DatabaseConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
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
}
