package application.server.configuration;

import application.server.configuration.types.ServerConfigType;

/**
 * The ServerConfigType class is a record class that holds the configuration for the server in the application.
 * It is supposed to be loaded from a JSON file.
 *
 * @param documentReservationServer The configuration for the document reservation.
 * @param documentBorrowServer      The configuration for the document borrow.
 * @param documentReturnServer      The configuration for the document return.
 */
public record ServerConfig(
        ServerConfigType documentReservationServer,
        ServerConfigType documentBorrowServer,
        ServerConfigType documentReturnServer
) {
    public ServerConfig {
        if (documentReservationServer == null) {
            throw new IllegalArgumentException("The document reservation configuration must not be null");
        }
        if (documentBorrowServer == null) {
            throw new IllegalArgumentException("The document borrow configuration must not be null");
        }
        if (documentReturnServer == null) {
            throw new IllegalArgumentException("The document return configuration must not be null");
        }
    }

    public static String getServerConfigFilePath() {
        return "./config/server_config.json";
    }
}
