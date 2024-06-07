package application.server.timer.tasks;

import application.server.domain.entities.interfaces.Abonne;
import application.server.timer.interfaces.AbstractTimerTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class BanUserTask extends AbstractTimerTask {
    private static final Logger LOGGER = LogManager.getLogger("Ban User Task");

    private static Optional<Long> defaultDurationInSeconds;
    private final Abonne simpleAbonne;
    private final Optional<Long> durationInSeconds;

    public BanUserTask(Abonne simpleAbonne, Long durationInSeconds) {
        this.simpleAbonne = simpleAbonne;
        this.durationInSeconds = Optional.ofNullable(durationInSeconds);
    }

    public BanUserTask(Abonne simpleAbonne) {
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
            LOGGER.error("The duration for the BanUserTask is not set");
            return new IllegalStateException("The duration must be set");
        });
    }

    @Override
    public void run() {
        simpleAbonne.unbanUser();
    }

    @Override
    public boolean isTimerCancelable() {
        return false;
    }

    public String getTaskIdentifier() {
        return "BanUserTask" + "-" + simpleAbonne.getId();
    }
}