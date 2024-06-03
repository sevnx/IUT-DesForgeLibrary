package application.server.domain;

import application.server.domain.core.*;
import application.server.models.DocumentModel;
import application.server.models.DvdModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Dvd extends AbstractDocument {
    private static int ADULT_AGE = 16;
    private SimpleDocument document;
    private boolean adult;


    @Override
    public void reservation(Abonne ab) throws ReservationException {
        if (this.adult && ab.getAge() < ADULT_AGE) {
            throw new ReservationException("Subscriber is not old enough to borrow this document");
        }
    }

    @Override
    public void emprunt(Abonne ab) throws EmpruntException {
        if (this.adult && ab.getAge() < ADULT_AGE) {
            throw new EmpruntException("Subscriber is not old enough to borrow this document");
        }
    }

    @Override
    public void retour() throws RetourException {
        this.document.retour();
    }

    @Override
    public Entity<DocumentModel> mapEntity(ResultSet resultSet) throws SQLException {
        this.setId(resultSet.getInt("id"));
        this.document = null;
        this.adult = resultSet.getBoolean("adult");

        return this;
    }

    @Override
    public void save() throws SQLException {
        synchronized (this.document) {
            new DvdModel().save(this);
        }
    }
}
