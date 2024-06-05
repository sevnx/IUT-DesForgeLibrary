package application.server.timer.interfaces;

public interface TimerConfig {
    String getIdentifier();

    long getDurationInSeconds();
}
