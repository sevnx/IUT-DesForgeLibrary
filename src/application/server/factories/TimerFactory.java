package application.server.factories;


import application.server.configuration.TimerConfig;
import application.server.domain.entities.interfaces.Abonne;
import application.server.domain.entities.types.DocumentLogEntity;
import application.server.domain.entities.types.SimpleDocumentEntity;
import application.server.managers.ConfigurationManager;
import application.server.managers.DataManager;
import application.server.managers.TimerManager;
import application.server.timer.tasks.BanUserTask;
import application.server.timer.tasks.BorrowTask;
import application.server.timer.tasks.ReservationTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TimerFactory {
    private static final Logger LOGGER = LogManager.getLogger("Timer Factory");

    public static void setupTimers() {
        LOGGER.info("Setting up timers");
        setupTimerTimes();
        setupDocumentTimers();
        setupUserTimers();
        LOGGER.info("Timers setup complete");
    }

    private static void setupTimerTimes() {
        LOGGER.debug("Setting up timer times (reading from configuration)");
        TimerConfig timerConfig = ConfigurationManager.getConfig(TimerConfig.class, TimerConfig.getTimerConfigFilePath());

        long banUserTimeInSeconds = getOrThrowIfZero(timerConfig.banUserTime().toSeconds());
        long borrowTimeInSeconds = getOrThrowIfZero(timerConfig.documentBorrowTime().toSeconds());
        long reservationTimeInSeconds = getOrThrowIfZero(timerConfig.documentReservationTime().toSeconds());

        BanUserTask.setDefaultDurationInSeconds(banUserTimeInSeconds);
        BorrowTask.setDefaultDurationInSeconds(borrowTimeInSeconds);
        ReservationTask.setDefaultDurationInSeconds(reservationTimeInSeconds);
        LOGGER.debug("Timer times set");
    }

    private static void setupDocumentTimers() {
        LOGGER.debug("Setting up document timers (reservations and borrows)");
        for (DocumentLogEntity log : DataManager.getDocumentLogs()) {
            SimpleDocumentEntity document = DataManager.getBaseDocument(log.getDocument().numero()).orElseThrow();
            switch (document.getState()) {
                case RESERVED -> {
                    Abonne subscriber = log.getSubscriber().orElseThrow();
                    long durationLeft = LocalDateTime.now().until(log.getTime().plusSeconds(ReservationTask.getDefaultDurationInSeconds()), ChronoUnit.SECONDS);
                    if (durationLeft > 0) {
                        ReservationTask task = new ReservationTask(subscriber, document, durationLeft);
                        TimerManager.startTimer(task.getTaskIdentifier(), task);
                    } else {
                        LOGGER.warn("Document {} reserved by {} has expired", document.numero(), subscriber.getId());
                        document.cancelReservation(subscriber);
                    }
                }
                case BORROWED -> {
                    Abonne subscriber = log.getSubscriber().orElseThrow();
                    long durationLeft = LocalDateTime.now().until(log.getTime().plusSeconds(BorrowTask.getDefaultDurationInSeconds()), ChronoUnit.SECONDS);
                    if (durationLeft > 0) {
                        BorrowTask task = new BorrowTask(subscriber, durationLeft);
                        TimerManager.startTimer(task.getTaskIdentifier(), task);
                    } else {
                        LOGGER.warn("Document {} borrowed by {} has expired", document.numero(), subscriber.getId());
                        try {
                            subscriber.banUser();
                        } catch (Exception e) {
                            LOGGER.error("Error returning document", e);
                        }
                    }
                }
                case FREE -> {
                }
            }
        }
        LOGGER.debug("Document timers set");
    }

    private static void setupUserTimers() {
        LOGGER.debug("Setting up user timers (bans)");
        for (Abonne subscriber : DataManager.getSubscribers()) {
            if (subscriber.isBanned() && subscriber.bannedUntil().isPresent()) {
                LocalDateTime bannedUntil = subscriber.bannedUntil().orElseThrow();
                long duration = bannedUntil.minusNanos(LocalDateTime.now().getNano()).getSecond();
                if (duration > 0) {
                    BanUserTask task = new BanUserTask(subscriber, duration);
                    TimerManager.startTimer(task.getTaskIdentifier(), task);
                }
            }
        }
        LOGGER.debug("User timers set");
    }

    private static long getOrThrowIfZero(long time) {
        if (time == 0) {
            throw new IllegalArgumentException("Time cannot be 0 seconds");
        }
        return time;
    }
}
