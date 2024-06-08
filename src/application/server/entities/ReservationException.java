package application.server.entities;

public class ReservationException extends RuntimeException {
    public ReservationException(String message) {
        super("RESERVATION IMPOSSIBLE: " + message);
    }
}
