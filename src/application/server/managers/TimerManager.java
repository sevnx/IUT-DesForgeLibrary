package application.server.managers;

import application.server.timer.interfaces.AbstractTimerTask;
import application.server.timer.interfaces.ControllableTimer;

import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

public class TimerManager {
    private static final ConcurrentHashMap<String, ControllableTimer> timerMap = new ConcurrentHashMap<>();

    public static void startTimer(String identifier, AbstractTimerTask timer) {
        Timer savedTimer = new Timer();
        savedTimer.schedule(timer,timer.getDurationInSeconds() * 1000);

        timerMap.put(identifier, new ControllableTimer(savedTimer, timer.isTimerCancelable()));
    }

    public static void stopTimer(String identifier) throws IllegalArgumentException {
        ControllableTimer timer = timerMap.get(identifier);

        if (timer != null) {
            timer.cancel();
            timerMap.remove(identifier);
        } else {
            throw new IllegalArgumentException("Timer not found");
        }
    }
}
