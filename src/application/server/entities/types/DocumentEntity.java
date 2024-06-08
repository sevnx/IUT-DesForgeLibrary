package application.server.entities.types;

import application.server.entities.Document;
import application.server.entities.Entity;
import application.server.models.types.DocumentModel;

/**
 * Represents the basic structure of a document entity.
 * A document is abstract as it can be of different types (DVD, etc.)
 */
public abstract class DocumentEntity implements Entity<DocumentModel>, Document {
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
}
