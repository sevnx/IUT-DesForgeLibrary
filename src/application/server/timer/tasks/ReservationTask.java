package application.server.timer.tasks;

import application.server.domain.entities.types.SimpleDocumentEntity;
import application.server.domain.entities.interfaces.Abonne;
import application.server.timer.interfaces.AbstractTimerTask;

public class ReservationTask extends AbstractTimerTask {
    private final Abonne simpleAbonne;
    private final SimpleDocumentEntity document;

    public ReservationTask(Abonne simpleAbonne, SimpleDocumentEntity document) {
        this.simpleAbonne = simpleAbonne;
        this.document = document;
    }

    @Override
    public String getIdentifier() {
        return "Reservation";
    }

    @Override
    public long getDurationInSeconds() {
        return 60 * 60 * 2; // 2 hours
    }

    @Override
    public void run() {
        try {
            document.cancelReservation(simpleAbonne);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isTimerCancelable() {
        return true;
    }
}
