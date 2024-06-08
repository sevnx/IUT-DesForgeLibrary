package application.server.timers.tasks;

import application.server.entities.Abonne;
import application.server.entities.types.SimpleDocumentEntity;
import application.server.timers.TimerTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

/**
 * Task of reserving a document for a certain duration.
 * If the person does not come to pick up the document in time, the reservation will be canceled
 */
public class ReservationTask extends TimerTask {
    private static final Logger LOGGER = LogManager.getLogger("Reservation Task");

    private static Optional<Long> defaultDurationInSeconds;
    private final Abonne simpleAbonne;
    private final SimpleDocumentEntity document;
    private final Optional<Long> durationInSeconds;

    public ReservationTask(Abonne simpleAbonne, SimpleDocumentEntity document, Long durationInSeconds) {
        this.simpleAbonne = simpleAbonne;
        this.document = document;
        this.durationInSeconds = Optional.ofNullable(durationInSeconds);
    }

    public ReservationTask(Abonne simpleAbonne, SimpleDocumentEntity document) {
        this(simpleAbonne, document, getDefaultDurationInSeconds());
    }

    public static long getDefaultDurationInSeconds() {
        return defaultDurationInSeconds.orElseThrow(() -> new IllegalStateException("The default duration must be set"));
    }

    public static void setDefaultDurationInSeconds(Long durationInSeconds) {
        if (durationInSeconds == null) {
            throw new IllegalArgumentException("Duration must be set");
        }
        if (durationInSeconds <= 0) {
            throw new IllegalArgumentException("Duration must be greater than 0");
        }
        defaultDurationInSeconds = Optional.of(durationInSeconds);
    }

    @Override
    public long getDurationInSeconds() {
        return durationInSeconds.orElseThrow(() -> {
            LOGGER.error("The duration for the ReservationTask is not set");
            return new IllegalStateException("The duration must be set");
        });
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
