package application.server.timer.tasks;

import application.server.domain.entities.interfaces.Abonne;
import application.server.timer.interfaces.AbstractTimerTask;

public class BanUserTask extends AbstractTimerTask {
    private final Abonne simpleAbonne;

    public BanUserTask(Abonne simpleAbonne) {
        this.simpleAbonne = simpleAbonne;
    }

    @Override
    public String getIdentifier() {
        return "BanUser";
    }

    @Override
    public long getDurationInSeconds() {
        return 60 * 60 * 24 * 60; // 60 days
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
}