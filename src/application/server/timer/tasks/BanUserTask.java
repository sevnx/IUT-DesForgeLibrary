package application.server.timer.tasks;

import application.server.domain.entities.interfaces.Abonne;
import application.server.timer.interfaces.AbstractTimerTask;

import java.util.Optional;

public class BanUserTask extends AbstractTimerTask {
    private final Abonne simpleAbonne;
    private final Optional<Long> durationInSeconds;

    public BanUserTask(Abonne simpleAbonne, Long durationInSeconds) {
        this.simpleAbonne = simpleAbonne;
        this.durationInSeconds = Optional.ofNullable(durationInSeconds);
    }

    public BanUserTask(Abonne simpleAbonne) {
        this(simpleAbonne, null);
    }

    public static long getDefaultDuration() {
        final int SECONDS = 60;
        final int MINUTES = 60;
        final int HOURS = 24;
        final int DAYS = 60;
        return SECONDS * MINUTES * HOURS * DAYS;
    }

    @Override
    public long getDurationInSeconds() {
        return durationInSeconds.orElse(getDefaultDuration());
    }

    @Override
    public void run() {
        try {
            simpleAbonne.unbanUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isTimerCancelable() {
        return false;
    }

    public String getTaskIdentifier() {
        return "BanUserTask" + "-" + simpleAbonne.getId();
    }
}