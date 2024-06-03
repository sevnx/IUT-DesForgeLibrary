package application.server.domain;

import application.server.domain.core.Abonne;
import application.server.domain.core.Document;
import application.server.domain.core.SimpleEntity;
import application.server.models.DocumentLogModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;

public class DocumentLog extends SimpleEntity<DocumentLogModel> {
    private int id;
    private Abonne subscriber;
    private Document document;
    private DocumentState newState;
    private Date time;

    public DocumentLog() {}

    public DocumentLog(int id, Abonne subscriber, Document document, DocumentState newState, Date time) {
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
    public DocumentLog mapEntity(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt("id");
        this.subscriber = null;
        this.document = null;
        this.newState = DocumentState.valueOf(resultSet.getString("new_state"));

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

    public Optional<Abonne> getSubscriber() {
        return Optional.ofNullable(subscriber);
    }
}
