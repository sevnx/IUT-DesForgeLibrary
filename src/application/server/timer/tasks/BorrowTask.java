package application.server.timer.tasks;

import application.server.domain.entities.interfaces.Abonne;
import application.server.timer.interfaces.AbstractTimerTask;

public class BorrowTask extends AbstractTimerTask {
    private final Abonne simpleAbonne;

    public BorrowTask(Abonne simpleAbonne) {
        this.simpleAbonne = simpleAbonne;
    }

    @Override
    public String getIdentifier() {
        return "Borrow";
    }

    @Override
    public long getDurationInSeconds() {
        return 60 * 60 * 24 * 30 + 60 * 60 * 24 * 14; // 30 days + 14 days
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
}
