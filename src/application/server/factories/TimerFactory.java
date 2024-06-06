package application.server.factories;


import application.server.domain.entities.interfaces.Abonne;
import application.server.domain.entities.types.DocumentLogEntity;
import application.server.domain.entities.types.SimpleDocumentEntity;
import application.server.managers.DataManager;
import application.server.managers.TimerManager;
import application.server.timer.tasks.BanUserTask;
import application.server.timer.tasks.BorrowTask;
import application.server.timer.tasks.ReservationTask;

import java.time.LocalDateTime;

public class TimerFactory {
    public static void setupTimers() {
        setupDocumentTimers();
        setupUserTimers();
    }

    private static void setupDocumentTimers(){
        for (DocumentLogEntity log : DataManager.getDocumentLogs()) {
            switch (log.getNewState()) {
                case RESERVED -> {
                    Abonne subscriber = log.getSubscriber().orElseThrow();
                    SimpleDocumentEntity document = log.getDocument();
                    ReservationTask task = new ReservationTask(subscriber, document);
                    TimerManager.startTimer(task.getTaskIdentifier(), task);
                }
                case BORROWED -> {
                    Abonne subscriber = log.getSubscriber().orElseThrow();
                    BorrowTask task = new BorrowTask(subscriber);
                    TimerManager.startTimer(task.getTaskIdentifier(), task);
                }
                case FREE -> {}
            }
        }
    }

    private static void setupUserTimers(){
        for (Abonne subscriber : DataManager.getSubscribers()) {
            if (subscriber.isBanned() && subscriber.bannedUntil().isPresent()) {
                LocalDateTime bannedUntil = subscriber.bannedUntil().orElseThrow();
                // Long required by Timer
                long duration = bannedUntil.minusNanos(LocalDateTime.now().getNano()).getSecond();
                if (duration > 0) {
                    BanUserTask task = new BanUserTask(subscriber, duration);
                    TimerManager.startTimer(task.getTaskIdentifier(), task);
                }
            }
        }
    }
}
