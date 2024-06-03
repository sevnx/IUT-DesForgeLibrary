package application.server.medialibrary;

import server.medialibrary.interfaces.ReservationException;

/**
* Duplicate with `{@link NotOldEnoughException} caused by interface signatures
*/
public class ReservationNotOldEnoughException extends ReservationException {
    public ReservationNotOldEnoughException() {
        super("Vous n’avez pas l’âge pour emprunter ce DVD");
    }
}
