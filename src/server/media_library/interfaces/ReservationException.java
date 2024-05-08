package server.media_library.interfaces;

public abstract class ReservationException extends DocumentException {
    public ReservationException(String message) {
        super(message);
    }
}
