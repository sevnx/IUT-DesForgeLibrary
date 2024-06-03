package application.server.domain;

import application.server.domain.core.*;
import application.server.models.DocumentModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Optional;

public class SimpleDocument extends AbstractDocument {
    private DocumentState state;
    private DocumentLog lastLog;

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
            case FREE -> processLog(ab, DocumentState.RESERVED);
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
                    processBorrow(ab);
                }
            }
            case FREE -> processBorrow(ab);
        }
    }

    public void processBorrow(Abonne ab) throws EmpruntException {
        processLog(ab, DocumentState.BORROWED);
    }

    public void processLog(Abonne ab, DocumentState newState) {
        try {
            synchronized (this) {
                this.state = newState;
                this.lastLog = new DocumentLog(getId(), ab, this, newState, Calendar.getInstance().getTime());
                this.lastLog.save();
            }
        } catch (SQLException ignored) {}
    }

    @Override
    public void retour() throws RetourException {
        switch (getState()) {
            case FREE -> throw new RetourException("Document is already free");
            case RESERVED -> throw new RetourException("Document is reserved");
            case BORROWED -> processLog(null, DocumentState.FREE);
        }
    }

    @Override
    public SimpleDocument mapEntity(ResultSet resultSet) throws SQLException {
        this.setId(resultSet.getInt("id"));
        this.setTitle(resultSet.getString("title"));

        Optional<DocumentState> state = DocumentState.fromInt(resultSet.getInt("state"));
        if (state.isPresent()) {
            this.state = state.get();
        } else {
            throw new SQLException("Invalid state");
        }

        return this;
    }

    @Override
    public String getEntityName() {
        return "";
    }

    @Override
    public void save() throws SQLException {
        synchronized (this) {
            new DocumentModel().save(this);
        }
    }
}
