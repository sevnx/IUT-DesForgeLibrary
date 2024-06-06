package application.server.timer.interfaces;

import java.util.TimerTask;

public abstract class AbstractTimerTask extends TimerTask implements TimerConfig {
    @Override
    public abstract String getTaskIdentifier();

    @Override
    public abstract long getDurationInSeconds();

    @Override
    public abstract void run();

    public abstract boolean isTimerCancelable();
}
