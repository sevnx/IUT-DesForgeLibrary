package application.server.managers;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * ConfigurationManager class
 * Manages the configs files for the application
 */
public class ConfigurationManager {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Gets the configs from the file path and returns it as an object of the specified class
     *
     * @param configClass the class of the configs object
     * @param filePath    the path to the configs file
     * @param <T>         the type of the configs object
     * @return the configs object
     */
    public static <T> T getConfig(Class<T> configClass, String filePath) {
        try {
            String json = new String(Files.readAllBytes(Paths.get(filePath)));
            return objectMapper.readValue(json, configClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
