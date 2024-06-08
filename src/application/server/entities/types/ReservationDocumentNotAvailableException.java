package application.server.entities.types;

import application.server.entities.ReservationException;

/**
 * Represents a special case of reservation exception.
 * This exception is thrown when a reservation is not available because the document isn't available.
 * It is used in the "BretteSoft - Sitting Bull" certification (mailing).
 */
public class ReservationDocumentNotAvailableException extends ReservationException {
    public ReservationDocumentNotAvailableException(String message) {
        super(message);
    }
}
