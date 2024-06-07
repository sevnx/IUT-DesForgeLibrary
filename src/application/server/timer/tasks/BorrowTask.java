package application.server.timer.tasks;

import application.server.domain.entities.interfaces.Abonne;
import application.server.timer.interfaces.AbstractTimerTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class BorrowTask extends AbstractTimerTask {
    private static final Logger LOGGER = LogManager.getLogger("Borrow Task");
    private static Optional<Long> defaultDurationInSeconds;
    private final Abonne simpleAbonne;
    private final Optional<Long> durationInSeconds;

    public BorrowTask(Abonne simpleAbonne, Long durationInSeconds) {
        if (durationInSeconds != null && durationInSeconds <= 0) {
            throw new IllegalArgumentException("Duration must be greater than 0");
        }
        this.simpleAbonne = simpleAbonne;
        this.durationInSeconds = Optional.ofNullable(durationInSeconds);
    }

    public BorrowTask(Abonne simpleAbonne) {
        this(simpleAbonne, getDefaultDurationInSeconds());
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
            LOGGER.error("The duration for the BorrowTask is not set");
            return new IllegalStateException("The duration must be set");
        });
    }

    @Override
    public void run() {
        try {
            simpleAbonne.banUser();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isTimerCancelable() {
        return true;
    }

    public String getTaskIdentifier() {
        return "BorrowTask" + "-" + simpleAbonne.getId();
    }
}
