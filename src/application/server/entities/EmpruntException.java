package application.server.entities;

public class EmpruntException extends RuntimeException {
    public EmpruntException(String message) {
        super("EMPRUNT IMPOSSIBLE: " + message);
    }
}
