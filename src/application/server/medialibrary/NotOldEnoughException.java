package server.media_library;

import server.media_library.interfaces.DocumentException;

/**
 * Duplicate with `{@link ReservationNotOldEnoughException} caused by interface signatures
 */
public class NotOldEnoughException extends DocumentException {
    public NotOldEnoughException() {
        super("Vous n’avez pas l’âge pour emprunter ce DVD.");
    }
}
