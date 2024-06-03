package application.server.managers;

import application.server.configuration.DatabaseConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConfigurationManager {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static DatabaseConfig getDatabaseConfig() {
        try {
            return objectMapper.readValue(DatabaseConfig.getDatabaseConfigFilePath(), DatabaseConfig.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
