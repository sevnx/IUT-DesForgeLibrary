package application.server.domain.entities.types;

import application.server.domain.entities.interfaces.Abonne;
import application.server.domain.entities.interfaces.EmpruntException;
import application.server.domain.entities.interfaces.ReservationException;
import application.server.domain.entities.interfaces.RetourException;
import application.server.domain.enums.DocumentState;
import application.server.managers.DataManager;
import application.server.managers.TimerManager;
import application.server.models.DocumentModel;
import application.server.timer.tasks.BorrowTask;
import application.server.timer.tasks.ReservationTask;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Optional;

public final class SimpleDocumentEntity extends DocumentEntity {
    private DocumentState state;
    private DocumentLogEntity lastLog;

    public DocumentState getState() {
        synchronized (this) {
            return state;
        }
    }

    @Override
    public void reservation(Abonne ab) throws ReservationException {
        switch (getState()) {
            case BORROWED -> throw new ReservationException("Document is already borrowed");
            case RESERVED -> throw new ReservationException("Document is already reserved");
            case FREE -> {
                TimerManager.startTimer("TODO",new ReservationTask(ab, this));
                processLog(ab, DocumentState.RESERVED);
            }
        }
    }

    @Override
    public void emprunt(Abonne ab) throws EmpruntException {
        switch (getState()) {
            case BORROWED -> throw new EmpruntException("Document is already borrowed");
            case RESERVED -> {
                synchronized (this) {
                    Optional<Abonne> subscriber = this.lastLog.getSubscriber();
                    if (subscriber.isEmpty() || !subscriber.get().equals(ab)) {
                        throw new EmpruntException("Document is reserved by another subscriber");
                    }
                    TimerManager.stopTimer("TODO");
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
            synchronized (this) {
                this.state = newState;
                this.lastLog = new DocumentLogEntity(getId(), ab, this, newState, LocalDateTime.now());
                this.lastLog.save();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while saving document log :", e);
        }
    }

    @Override
    public void retour() throws RetourException {
        switch (getState()) {
            case FREE -> throw new RetourException("Document is already free");
            case RESERVED -> throw new RetourException("Document is reserved");
            case BORROWED -> {
                processLog(null, DocumentState.FREE);
                Abonne ab = this.lastLog.getSubscriber().orElseThrow(() -> new RetourException("No subscriber found"));
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

        Optional<DocumentState> state = DocumentState.fromInt(resultSet.getInt("idState"));
        if (state.isPresent()) {
            this.state = state.get();
        } else {
            throw new SQLException("Invalid state");
        }

        this.lastLog = DataManager.getDocumentLog(resultSet.getInt("id")).orElse(null);

        return this;
    }

    @Override
    public void save() throws SQLException {
        synchronized (this) {
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
