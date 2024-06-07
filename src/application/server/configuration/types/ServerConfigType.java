package application.server.configuration.types;

/**
 * The ServerConfigType class is a record
 * class that holds the configuration for the server in the application.
 * It is supposed to be loaded from a JSON file.
 *
 * @param port The port that the server will listen to.
 */
public record ServerConfigType(
        int port
) {
    public ServerConfigType {
        if (port < 0) {
            throw new IllegalArgumentException("The port must be greater than or equal to 0");
        }
    }
}
