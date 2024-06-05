package application.server.domain.entities.types;

import application.server.domain.entities.interfaces.Abonne;
import application.server.domain.entities.interfaces.Document;
import application.server.domain.enums.DocumentState;
import application.server.managers.DataManager;
import application.server.models.DocumentLogModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

public class DocumentLogEntity extends SimpleEntity<DocumentLogModel> {
    private int id;
    private Abonne subscriber;
    private Document document;
    private DocumentState newState;
    private LocalDateTime time;

    public DocumentLogEntity() {}

    public DocumentLogEntity(int id, Abonne subscriber, Document document, DocumentState newState, LocalDateTime time) {
        this.id = id;
        this.subscriber = subscriber;
        this.document = document;
        this.newState = newState;
        this.time = time;
    }

    @Override
    public int getId() {
        return id;
    }

    public DocumentState getNewState() {
        return newState;
    }

    @Override
    public DocumentLogEntity mapEntity(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt("id");
        this.subscriber = DataManager.getSubscriber(resultSet.getInt("idSubscriber")).orElse(null);
        this.document = DataManager.getDocument(resultSet.getInt("idDocument")).orElseThrow();
        this.newState = DocumentState.valueOf(resultSet.getString("idNewState"));
        this.time = resultSet.getTimestamp("time").toLocalDateTime();
        return this;
    }

    @Override
    public String getEntityName() {
        return "DocumentLog";
    }

    @Override
    public void save() throws SQLException {
        synchronized (this) {
            new DocumentLogModel().save(this);
        }
    }

    public LocalDateTime getTime() {
        return time;
    }

    public Optional<Abonne> getSubscriber() {
        return Optional.ofNullable(subscriber);
    }

    public Document getDocument() {
        return document;
    }
}
