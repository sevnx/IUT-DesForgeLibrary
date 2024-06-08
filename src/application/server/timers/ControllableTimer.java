package application.server.timers;

import java.util.Timer;

/**
 * A timers that can be controlled
 * It is useful to choose whether the timers can be cancelled or not
 * (e.g. a ban timers should not be cancellable)
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
