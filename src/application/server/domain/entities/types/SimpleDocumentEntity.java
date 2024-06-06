package application.server.domain.entities.types;

import application.server.domain.entities.interfaces.Abonne;
import application.server.domain.entities.interfaces.EmpruntException;
import application.server.domain.entities.interfaces.ReservationException;
import application.server.domain.entities.interfaces.RetourException;
import application.server.domain.enums.DocumentState;
import application.server.managers.TimerManager;
import application.server.models.DocumentModel;
import application.server.timer.tasks.BorrowTask;
import application.server.timer.tasks.ReservationTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public final class SimpleDocumentEntity extends DocumentEntity {
    private static final Logger LOGGER = LogManager.getLogger("Base Document Entity");
    private final Object stateLock = new Object();
    private DocumentState state;
    private Optional<DocumentLogEntity> lastLog;

    public DocumentState getState() {
        synchronized (stateLock) {
            return state;
        }
    }

    /**
     * Set the state of the document
     * This is needed as when we load the document from the database,
     * we can't immediately get the last log as it is not loaded yet
     *
     * @param lastLog the last log of the document
     */
    public void setLastLog(DocumentLogEntity lastLog) {
        this.lastLog = Optional.of(lastLog);
    }

    @Override
    public void reservation(Abonne ab) throws ReservationException {
        LOGGER.info("Trying to reserve document {} with state {} by subscriber {}", this.numero(), getState(), ab.getId());
        switch (getState()) {
            case BORROWED -> throw new ReservationException("Ce document est déjà emprunté");
            case RESERVED -> {
                synchronized (stateLock) {
                    if (!Objects.equals(this.lastLog.orElseThrow().getSubscriber().orElse(null), ab)) {
                        throw new ReservationException("Ce document est déjà réservé par un autre abonné");
                    } else {
                        throw new ReservationException("Vous avez déjà réservé ce document");
                    }
                }
            }
            case FREE -> {
                synchronized (stateLock) {
                    TimerManager.startTimer("TODO", new ReservationTask(ab, this));
                    processLog(ab, DocumentState.RESERVED);
                }
            }
        }
    }

    @Override
    public void emprunt(Abonne ab) throws EmpruntException {
        LOGGER.info("Trying to borrow document {} with state {} by subscriber {}", this.numero(), getState(), ab.getId());
        switch (getState()) {
            case BORROWED -> throw new EmpruntException("Document is already borrowed");
            case RESERVED -> {
                synchronized (stateLock) {
                    Optional<Abonne> subscriber = this.lastLog
                            .orElseThrow(() -> new RuntimeException("Dernier log introuvable"))
                            .getSubscriber();
                    if (subscriber.isEmpty()) {
                        throw new RuntimeException("L'abonné n'a pas été trouvé");
                    }
                    if (!subscriber.get().equals(ab)) {
                        throw new EmpruntException("Document is reserved by another subscriber");
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

    public void processLog(Abonne ab, DocumentState newState) {
        try {
            synchronized (stateLock) {
                this.state = newState;
                this.save();
                DocumentLogEntity newLog = new DocumentLogEntity(ab, this, LocalDateTime.now());
                newLog.save();
                this.lastLog = Optional.of(newLog);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while saving document log :", e);
        }
    }

    @Override
    public void retour() throws RetourException {
        LOGGER.info("Trying to return document {} with state {}", this.numero(), getState());
        switch (getState()) {
            case FREE -> throw new RetourException("Document is already free");
            case RESERVED -> throw new RetourException("Document is reserved");
            case BORROWED -> {
                Abonne ab = this.lastLog
                        .orElseThrow(() -> new RuntimeException("Dernier log introuvable"))
                        .getSubscriber()
                        .orElseThrow(() -> new RetourException("Abonné introuvable"));
                processLog(null, DocumentState.FREE);
                try {
                    BorrowTask task = new BorrowTask(ab);
                    TimerManager.stopTimer(task.getTaskIdentifier());
                } catch (IllegalArgumentException e) {
                    throw new RetourException("Vous avez été banis");
                }
            }
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
    }

    @Override
    public String toString() {
        return this.numero() + " - " + this.getTitle() + " (" + this.state.toString() + ")";
    }
}
