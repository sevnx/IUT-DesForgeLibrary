package application.server.factories;

import application.server.domain.entities.types.DocumentEntity;
import application.server.domain.entities.types.DocumentLogEntity;
import application.server.domain.entities.types.SimpleAbonneEntity;
import application.server.domain.entities.types.SimpleDocumentEntity;
import application.server.managers.DataManager;
import application.server.models.DocumentLogModel;
import application.server.models.DocumentModel;
import application.server.models.DvdModel;
import application.server.models.SubscriberModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.Vector;

public class DataFactory {
    private static final Logger LOGGER = LogManager.getLogger("Data Factory");

    public static void populateData() {
        LOGGER.info("Populating data");
        try {
            populateSubscribers();
            populateBaseDocuments();
            populateDocuments();
            populateDocumentLogs();
        } catch (SQLException e) {
            LOGGER.error("Error while fetching data from database : {}", e.getMessage());
            throw new RuntimeException("Error while fetching data from database :", e);
        }
        LOGGER.info("Data population complete");
    }

    private static void populateDocumentLogs() throws SQLException {
        LOGGER.debug("Populating document logs");
        Vector<DocumentLogEntity> logs = new DocumentLogModel().get();
        for (DocumentLogEntity log : logs) {
            DataManager.addDocumentLog(log);
        }
    }

    private static void populateBaseDocuments() throws SQLException {
        LOGGER.debug("Populating base documents");
        Vector<SimpleDocumentEntity> documents = new DocumentModel().get();
        for (SimpleDocumentEntity document : documents) {
            DataManager.addBaseDocument(document);
        }
    }

    private static void populateSubscribers() throws SQLException {
        LOGGER.debug("Populating subscribers");
        for (SimpleAbonneEntity subscriber : new SubscriberModel().get()) {
            DataManager.addSubscriber(subscriber);
        }
    }

    private static void populateDocuments() throws SQLException {
        LOGGER.debug("Populating documents");
        for (DocumentEntity document : new DvdModel().get()) {
            DataManager.addDocument(document);
        }
    }
}
