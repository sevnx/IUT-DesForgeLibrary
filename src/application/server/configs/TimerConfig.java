package application.server.configs;

import application.server.configs.components.TimeConfigType;

import java.util.Objects;

/**
 * The TimerConfig class is a record class that holds the configs for the timers in the application.
 * It is supposed to be loaded from a JSON file.
 * It is used to configure the different timers in the application.
 *
 * @param banUserTime             The time for which a user is banned.
 * @param documentBorrowTime      The time for which a user can borrow a book.
 * @param documentReservationTime The time for which a user can return a book.
 */
public record TimerConfig(
        TimeConfigType banUserTime,
        TimeConfigType documentBorrowTime,
        TimeConfigType documentMaxLateReturnTime,
        TimeConfigType documentReservationTime
) {
    private static final String TIMER_CONFIG_FILE_PATH = "./config/timer_config.json";

    public TimerConfig {
        Objects.requireNonNull(banUserTime, "The ban user time must not be null");
        Objects.requireNonNull(documentBorrowTime, "The borrow time must not be null");
        Objects.requireNonNull(documentMaxLateReturnTime, "The max late return time must not be null");
        Objects.requireNonNull(documentReservationTime, "The reservation time must not be null");
    }

    public static String getTimerConfigFilePath() {
        return TIMER_CONFIG_FILE_PATH;
    }
}

