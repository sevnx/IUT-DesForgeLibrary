package server.media_library;

import server.media_library.interfaces.ReservationException;

/**
* Duplicate with `{@link NotOldEnoughException} caused by interface signatures
*/
public class ReservationNotOldEnoughException extends ReservationException {
    public ReservationNotOldEnoughException() {
        super("Vous n’avez pas l’âge pour emprunter ce DVD");
    }
}
