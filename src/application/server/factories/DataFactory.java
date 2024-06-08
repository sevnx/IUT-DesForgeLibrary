package application.server.factories;

import application.server.entities.types.DocumentEntity;
import application.server.entities.types.DocumentLogEntity;
import application.server.entities.types.SimpleAbonneEntity;
import application.server.entities.types.SimpleDocumentEntity;
import application.server.managers.DataManager;
import application.server.models.Model;
import application.server.models.types.DocumentLogModel;
import application.server.models.types.DocumentModel;
import application.server.models.types.DvdModel;
import application.server.models.types.SubscriberModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * DataFactory class
 * Populates the data in the DataManager from the database
 */
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

    private static List<Model<? extends DocumentEntity>> getDocumentTypeModels() {
        List<Model<? extends DocumentEntity>> models = new ArrayList<>();
        models.add(new DvdModel());
        return models;
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
        for (Model<? extends DocumentEntity> model : getDocumentTypeModels()) {
            for (DocumentEntity document : model.get()) {
                DataManager.addDocument(document);
            }
        }
    }
}
