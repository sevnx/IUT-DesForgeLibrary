package application.server.entities.types;

import application.server.entities.Abonne;
import application.server.entities.EmpruntException;
import application.server.entities.ReservationException;
import application.server.entities.RetourException;
import application.server.managers.MailReminderManager;
import application.server.managers.TimerManager;
import application.server.models.types.DocumentModel;
import application.server.timers.tasks.UnbanUserTask;
import application.server.timers.tasks.BorrowTask;
import application.server.timers.tasks.ReservationTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Represents a simple document entity.
 * A simple document is a document that can be borrowed, reserved, and returned.
 */
public final class SimpleDocumentEntity extends DocumentEntity {
    private static final Logger LOGGER = LogManager.getLogger("Base Document Entity");
    private final Object logLock = new Object();
    private final Object stateLock = new Object();
    private DocumentState state;
    private Optional<DocumentLogEntity> lastLog;

    private static final String ALREADY_BORROWED_MESSAGE = "Ce document est déjà emprunté par un autre abonné";
    private static final String ALREADY_RESERVED_MESSAGE = "Ce document est déjà réservé par un autre abonné";
    private static final String BORROWED_SELF_MESSAGE = "Vous avez déjà emprunté ce document";
    private static final String RESERVED_SELF_MESSAGE = "Vous avez déjà réservé ce document";

    private static void logOptionalError(DocumentState state, String notFoundElement) {
        LOGGER.error("{} not found in {} log - (THIS SHOULD NOT HAPPEN)", notFoundElement, state.getName());
    }

    /**
     * A synchronized function to get the last log of the document
     *
     * @return the last log of the document
     */
    public DocumentState getState() {
        synchronized (stateLock) {
            return state;
        }
    }

    /**
     * A synchronized function to set the current state of the document
     * This is also needed as when we load the document from the database,
     * we can't immediately get the last log as it is not loaded yet
     *
     * @param lastLog the last log of the document
     */
    public void setLastLog(DocumentLogEntity lastLog) {
        this.lastLog = Optional.of(lastLog);
    }

    @Override
    public void reservation(Abonne ab) throws ReservationException {
        LOGGER.debug("Trying to reserve document {} with state {} by subscriber {}", this.numero(), getState(), ab.getId());
        checkForBan(ab);
        synchronized (stateLock) {
            switch (getState()) {
                case BORROWED -> {
                    final String ALREADY_BORROWED_MESSAGE = "Ce document est déjà emprunté par un autre abonné";
                    Abonne subscriber = getAbonneFromLastLog(ALREADY_BORROWED_MESSAGE);
                    if (!subscriber.equals(ab)) {
                        throw new ReservationDocumentNotAvailableException(ALREADY_BORROWED_MESSAGE);
                    } else {
                        throw new ReservationException("Vous avez déjà emprunté ce document");
                    }
                }
                case RESERVED -> {
                    final String ALREADY_RESERVED_MESSAGE = "Ce document est déjà réservé par un autre abonné";
                    Abonne subscriber = getAbonneFromLastLog(ALREADY_RESERVED_MESSAGE);
                    if (!subscriber.equals(ab)) {
                        throw new ReservationDocumentNotAvailableException(ALREADY_RESERVED_MESSAGE);
                    } else {
                        throw new ReservationException("Vous avez déjà réservé ce document");
                    }
                }
                case FREE -> {
                    ReservationTask task = new ReservationTask(ab, this);
                    TimerManager.startTimer(task.getTaskIdentifier(), task);
                    processLog(ab, DocumentState.RESERVED);
                }
            }
        }
    }

    @Override
    public void emprunt(Abonne ab) throws EmpruntException {
        LOGGER.debug("Trying to borrow document {} with state {} by subscriber {}", this.numero(), getState(), ab.getId());
        checkForBan(ab);
        switch (getState()) {
            case BORROWED -> {
                final String ALREADY_BORROWED_MESSAGE = "Ce document est déjà emprunté par un autre abonné";
                Abonne subscriber = getAbonneFromLastLog(ALREADY_BORROWED_MESSAGE);
                if (!subscriber.equals(ab)) {
                    throw new EmpruntException(ALREADY_BORROWED_MESSAGE);
                } else {
                    throw new EmpruntException("Vous avez déjà emprunté ce document");
                }
            }
            case RESERVED -> {
                synchronized (stateLock) {
                    Abonne subscriber = getAbonneFromLastLog(ALREADY_RESERVED_MESSAGE);
                    if (!subscriber.equals(ab)) {
                        throw new EmpruntException(ALREADY_RESERVED_MESSAGE);
                    }
                    ReservationTask task = new ReservationTask(ab, this);
                    TimerManager.stopTimer(task.getTaskIdentifier());
                    processBorrow(ab);
                }
            }
            case FREE -> processBorrow(ab);
        }
    }

    public void processBorrow(Abonne ab) throws EmpruntException {
        BorrowTask task = new BorrowTask(ab);
        TimerManager.startTimer(task.getTaskIdentifier(), task);
        processLog(ab, DocumentState.BORROWED);
    }

    @Override
    public void retour() throws RetourException {
        synchronized (stateLock) {
            DocumentState currentState = getState();
            LOGGER.debug("Trying to return document {} with state {}", this.numero(), currentState);
            switch (currentState) {
                case FREE -> throw new RetourException("Ce document n'est pas emprunté");
                case RESERVED -> throw new RetourException("Ce document est actuellement réservé");
                case BORROWED -> {
                    Abonne ab = getAbonneFromLastLog("Erreur Serveur");
                    processLog(null, DocumentState.FREE);
                    MailReminderManager.sendReminder(this);
                    try {
                        BorrowTask task = new BorrowTask(ab);
                        TimerManager.stopTimer(task.getTaskIdentifier());
                    } catch (IllegalArgumentException e) {
                        LOGGER.debug("Banned user returned document, starting ban timer");
                        long duration = UnbanUserTask.getDefaultDurationInSeconds();
                        UnbanUserTask banUserTask = new UnbanUserTask(ab, duration);
                        ab.setBannedUntil(LocalDateTime.now().plusSeconds(duration));
                        try {
                            ab.save();
                        } catch (SQLException ex) {
                            LOGGER.error("Failed to save abonne", ex);
                            throw new RuntimeException("Failed to save abonne", ex);
                        }
                        TimerManager.startTimer(banUserTask.getTaskIdentifier(), banUserTask);
                        String bannedUntil = ab.bannedUntil().isPresent() ? ab.bannedUntil().get().toString() : "indéfini";
                        throw new RetourException("Vous avez été banis jusqu'au " + bannedUntil);
                    }
                }
            }
        }
    }

    public void processLog(Abonne ab, DocumentState newState) {
        try {
            synchronized (stateLock) {
                this.state = newState;
                this.save();
                synchronized (logLock) {
                    if (this.lastLog.isEmpty()) {
                        this.lastLog = Optional.of(new DocumentLogEntity());
                    }
                    DocumentLogEntity log = this.lastLog.orElseGet(DocumentLogEntity::new);
                    log.setNewLog(ab, this);
                    log.save();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while saving document log :", e);
        }
    }

    @Override
    public SimpleDocumentEntity mapEntity(ResultSet resultSet) throws SQLException {
        this.setId(resultSet.getInt("id"));
        this.setTitle(resultSet.getString("title"));
        this.state = DocumentState.fromInt(resultSet.getInt("idState")).orElseThrow();
        this.lastLog = Optional.empty();

        return this;
    }

    @Override
    public void save() throws SQLException {
        synchronized (stateLock) {
            new DocumentModel().save(this);
        }
    }

    public void cancelReservation(Abonne abonne) {
        this.processLog(abonne, DocumentState.FREE);
        MailReminderManager.sendReminder(this);
    }

    @Override
    public String toString() {
        return this.numero() + " - " + this.getTitle() + " (" + this.state.getName() + ")";
    }

    private Abonne getAbonneFromLastLog(String defaultLogErrorMessage) {
        synchronized (logLock) {
            return this.lastLog
                    .orElseThrow(() -> {
                        logOptionalError(state, "Last log");
                        return new RuntimeException(defaultLogErrorMessage);
                    })
                    .getSubscriber()
                    .orElseThrow(() -> {
                        logOptionalError(state, "Subscriber");
                        return new RuntimeException(defaultLogErrorMessage);
                    });
        }
    }

    private void checkForBan(Abonne ab) throws ReservationException {
        if (ab.isBanned()) {
            String bannedUntil = ab.bannedUntil().isPresent() ? ab.bannedUntil().get().toString() : "indéfini";
            throw new ReservationException("Vous avez été bannis jusqu'au " + bannedUntil);
        }
    }
}