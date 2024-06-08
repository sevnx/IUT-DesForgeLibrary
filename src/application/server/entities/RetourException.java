package application.server.entities;

public class RetourException extends RuntimeException {
    public RetourException(String message) {
        super("PROBLEME SURVENU PENDANT LE RETOUR: " + message);
    }
}
