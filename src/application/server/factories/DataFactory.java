package application.server.factories;

import application.server.domain.entities.interfaces.Abonne;
import application.server.domain.entities.interfaces.Entity;
import application.server.domain.entities.types.DocumentEntity;
import application.server.domain.entities.types.DocumentLogEntity;
import application.server.domain.entities.types.SimpleAbonneEntity;
import application.server.domain.entities.types.SimpleDocumentEntity;
import application.server.managers.DataManager;
import application.server.models.*;

import java.sql.SQLException;
import java.util.Vector;

public class DataFactory {
    public static void populateData() {
        try {
            populateSubscribers();
            populateBaseDocuments();
            populateDocuments();
            populateDocumentLogs();
        } catch (SQLException e) {
            throw new RuntimeException("Error while fetching data from database :", e);
        }
    }

    private static void populateDocumentLogs() throws SQLException {
        Vector<DocumentLogEntity> logs = new DocumentLogModel().get();
        for (DocumentLogEntity log : logs) {
            DataManager.addDocumentLog(log);
        }
    }

    private static void populateBaseDocuments() throws SQLException {
        Vector<SimpleDocumentEntity> documents = new DocumentModel().get();
        for (SimpleDocumentEntity document : documents) {
            DataManager.addBaseDocument(document);
        }
    }

    private static void populateSubscribers() throws SQLException {
        for (SimpleAbonneEntity subscriber : new SubscriberModel().get()) {
            DataManager.addSubscriber(subscriber);
        }
    }

    private static void populateDocuments() throws SQLException {
        for (DocumentEntity document : new DvdModel().get()) {
            DataManager.addDocument(document);
        }
    }
}
