package application.server.domain.entities.types;

import application.server.domain.entities.interfaces.Abonne;
import application.server.managers.DataManager;
import application.server.models.DocumentLogModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

public class DocumentLogEntity extends SimpleEntity<DocumentLogModel> {
    private Abonne subscriber;
    private SimpleDocumentEntity document;
    private LocalDateTime time;

    public DocumentLogEntity() {
    }

    public DocumentLogEntity(Abonne subscriber, SimpleDocumentEntity document, LocalDateTime time) {
        this.subscriber = subscriber;
        this.document = document;
        this.time = time;
    }

    @Override
    public int getId() {
        return document.numero();
    }

    @Override
    public DocumentLogEntity mapEntity(ResultSet resultSet) throws SQLException {
        this.document = DataManager.getBaseDocument(resultSet.getInt("id")).orElseThrow();
        this.subscriber = DataManager.getSubscriber(resultSet.getInt("idSubscriber")).orElse(null);
        this.time = resultSet.getTimestamp("time").toLocalDateTime();
        this.document.setLastLog(this);
        return this;
    }

    @Override
    public String getEntityName() {
        return "DocumentChangeLog";
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

    public SimpleDocumentEntity getDocument() {
        return document;
    }
}
