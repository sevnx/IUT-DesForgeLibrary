package application.server.domain.entities.interfaces;

public class ReservationException extends RuntimeException {
    public ReservationException(String message) {
        super("RESERVATION IMPOSSIBLE: " + message);
    }
}
