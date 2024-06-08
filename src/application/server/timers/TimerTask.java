package application.server.timers;

/**
 * Abstract TimerTask with information about the identifier, duration and cancellability
 */
public abstract class TimerTask extends java.util.TimerTask {
    public abstract String getTaskIdentifier();

    public abstract long getDurationInSeconds();

    public abstract boolean isTimerCancelable();
}
