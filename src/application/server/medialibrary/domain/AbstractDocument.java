package application.server.medialibrary.domain;

import application.server.medialibrary.domain.core.Document;

public abstract class AbstractDocument implements Document {
    private int id;
    private String title;

    public int numero() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
