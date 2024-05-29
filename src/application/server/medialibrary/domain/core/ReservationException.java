package application.server.medialibrary.domain.core;

public abstract class ReservationException extends DocumentException {
    public ReservationException(String message) {
        super(message);
    }
}
