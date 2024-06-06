package application.server.timer.tasks;

import application.server.domain.entities.interfaces.Abonne;
import application.server.timer.interfaces.AbstractTimerTask;

import java.util.Optional;

public class BorrowTask extends AbstractTimerTask {
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
        this(simpleAbonne, null);
    }

    @Override
    public long getDurationInSeconds() {
        return durationInSeconds.orElse(getDefaultDuration());
    }

    public static long getDefaultDuration() {
        final int SECONDS = 60;
        final int MINUTES = 60;
        final int HOURS = 24;
        final int DAYS = 30;
        final int MAX_LATE_DAYS = 14;
        return SECONDS * MINUTES * HOURS * (DAYS + MAX_LATE_DAYS);
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
