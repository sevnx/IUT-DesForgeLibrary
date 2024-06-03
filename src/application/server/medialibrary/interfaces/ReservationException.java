package application.server.medialibrary.interfaces;

public abstract class ReservationException extends DocumentException {
    public ReservationException(String message) {
        super(message);
    }
}
