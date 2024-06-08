package application.server.configs;

import application.server.configs.components.ServerConfigType;

/**
 * The ServerConfigType class is a record class that holds the configs for the server in the application.
 * It is supposed to be loaded from a JSON file.
 *
 * @param documentReservationServer The configs for the document reservation.
 * @param documentBorrowServer      The configs for the document borrow.
 * @param documentReturnServer      The configs for the document return.
 */
public record ServerConfig(
        ServerConfigType documentReservationServer,
        ServerConfigType documentBorrowServer,
        ServerConfigType documentReturnServer
) {
    public ServerConfig {
        if (documentReservationServer == null) {
            throw new IllegalArgumentException("The document reservation configs must not be null");
        }
        if (documentBorrowServer == null) {
            throw new IllegalArgumentException("The document borrow configs must not be null");
        }
        if (documentReturnServer == null) {
            throw new IllegalArgumentException("The document return configs must not be null");
        }
    }

    public static String getServerConfigFilePath() {
        return "./config/server_config.json";
    }
}

