package application.server.domain;

import application.server.domain.core.Document;
import application.server.domain.core.SimpleEntity;
import application.server.models.DocumentModel;

public abstract class AbstractDocument extends SimpleEntity<DocumentModel> implements Document {
    private int id;
    private String title;

    public int numero() {
        return id;
    }

    public int getId() {
        return numero();
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEntityName() {
        return "Document";
    }


}
