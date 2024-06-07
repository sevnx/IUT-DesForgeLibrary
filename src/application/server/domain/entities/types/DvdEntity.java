package application.server.domain.entities.types;

import application.server.domain.entities.interfaces.*;
import application.server.managers.DataManager;
import application.server.models.DocumentModel;
import application.server.models.DvdModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DvdEntity extends DocumentEntity {
    private static final String NOT_OLD_ENOUGH_MESSAGE = "Vous n'avez pas l'âge requis pour réserver ce document";
    private static final int ADULT_AGE = 16;
    private final Object dvdLock = new Object();
    private SimpleDocumentEntity document;
    private boolean adult;

    @Override
    public void reservation(Abonne ab) throws ReservationException {
        if (this.adult && ab.getAge() < ADULT_AGE) {
            throw new ReservationException(NOT_OLD_ENOUGH_MESSAGE);
        }
        document.reservation(ab);
    }

    @Override
    public void emprunt(Abonne ab) throws EmpruntException {
        if (this.adult && ab.getAge() < ADULT_AGE) {
            throw new EmpruntException(NOT_OLD_ENOUGH_MESSAGE);
        }
        document.emprunt(ab);
    }

    @Override
    public void retour() throws RetourException {
        this.document.retour();
    }

    @Override
    public Entity<DocumentModel> mapEntity(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        this.setId(id);
        this.document = DataManager.getBaseDocument(id).orElse(null);
        this.adult = resultSet.getBoolean("isForAdult");

        return this;
    }

    @Override
    public String getEntityName() {
        return "Dvd";
    }

    @Override
    public void save() throws SQLException {
        synchronized (dvdLock) {
            new DvdModel().save(this);
        }
    }

    @Override
    public String toString() {
        return this.document.toString() + " - " + (adult ? "PG16" : "Tout public");
    }
}
