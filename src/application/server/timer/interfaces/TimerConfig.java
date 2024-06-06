package application.server.timer.interfaces;

public interface TimerConfig {
    String getTaskIdentifier();

    long getDurationInSeconds();
}
