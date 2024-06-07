package application.server.domain.entities.types;

import application.server.domain.entities.interfaces.ReservationException;

public class ReservationNotAvailableException extends ReservationException {
    public ReservationNotAvailableException(String message) {
        super(message);
    }
}
