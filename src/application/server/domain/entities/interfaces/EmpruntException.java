package application.server.domain.entities.interfaces;

public class EmpruntException extends RuntimeException {
    public EmpruntException(String message) {
        super("EMPRUNT IMPOSSIBLE: " + message);
    }
}
