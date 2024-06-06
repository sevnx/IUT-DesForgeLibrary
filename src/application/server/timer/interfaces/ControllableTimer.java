package application.server.timer.interfaces;

import java.util.Timer;

/**
 * A timer that can be controlled
 * It is useful to choose whether the timer can be cancelled or not
 * (e.g. a ban timer should not be cancellable)
 *
 * @see Timer
 */
public class ControllableTimer {
    private final Timer timer;
    private final boolean cancellable;

    public ControllableTimer(Timer timer, boolean cancellable) {
        this.timer = timer;
        this.cancellable = cancellable;
    }

    public void cancel() {
        if (cancellable) {
            timer.cancel();
        } else {
            throw new IllegalStateException("Timer is not cancellable");
        }
    }
}
