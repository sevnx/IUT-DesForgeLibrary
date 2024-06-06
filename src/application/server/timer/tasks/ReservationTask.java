package application.server.timer.tasks;

import application.server.domain.entities.interfaces.Abonne;
import application.server.domain.entities.types.SimpleDocumentEntity;
import application.server.timer.interfaces.AbstractTimerTask;

import java.util.Optional;

public class ReservationTask extends AbstractTimerTask {
    private final Abonne simpleAbonne;
    private final SimpleDocumentEntity document;
    private final Optional<Long> durationInSeconds;

    public ReservationTask(Abonne simpleAbonne, SimpleDocumentEntity document, Long durationInSeconds) {
        this.simpleAbonne = simpleAbonne;
        this.document = document;
        this.durationInSeconds = Optional.ofNullable(durationInSeconds);
    }

    public ReservationTask(Abonne simpleAbonne, SimpleDocumentEntity document) {
        this(simpleAbonne, document, null);
    }

    public static long getDefaultDurationInSeconds() {
        long SECONDS = 60;
        long MINUTES = 60;
        long HOURS = 2;
        return SECONDS * MINUTES * HOURS;
    }

    @Override
    public long getDurationInSeconds() {
        return durationInSeconds.orElse(getDefaultDurationInSeconds());
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

    public String getTaskIdentifier() {
        return "ReservationTask" + "-" + simpleAbonne.getId() + "-" + document.getId();
    }
}
