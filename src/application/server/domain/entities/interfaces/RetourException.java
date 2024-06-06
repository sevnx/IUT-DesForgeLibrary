package application.server.domain.entities.interfaces;

public class RetourException extends RuntimeException {
    public RetourException(String message) {
        super("RETOUR IMPOSSIBLE: " + message);
    }
}
