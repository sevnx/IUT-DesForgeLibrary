package application.server.managers;

import application.server.entities.Abonne;
import application.server.entities.Document;
import application.server.entities.types.DocumentLogEntity;
import application.server.entities.types.SimpleDocumentEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The DataManager class is responsible for managing the data in the application
 */
public class DataManager {
    private static final ConcurrentHashMap<Integer, Document> documents = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, Abonne> subscribers = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<Integer, SimpleDocumentEntity> baseDocuments = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, DocumentLogEntity> documentLogs = new ConcurrentHashMap<>();

    public static void addDocument(Document document) {
        documents.put(document.numero(), document);
    }

    public static void addSubscriber(Abonne abonne) {
        subscribers.put(abonne.getId(), abonne);
    }

    public static void addBaseDocument(SimpleDocumentEntity document) {
        baseDocuments.put(document.getId(), document);
    }

    public static void addDocumentLog(DocumentLogEntity documentLog) {
        documentLogs.put(documentLog.getId(), documentLog);
    }

    public static Optional<Document> getDocument(int id) {
        return Optional.ofNullable(documents.get(id));
    }

    public static Optional<Abonne> getSubscriber(int id) {
        return Optional.ofNullable(subscribers.get(id));
    }

    public static Optional<SimpleDocumentEntity> getBaseDocument(int id) {
        return Optional.ofNullable(baseDocuments.get(id));
    }

    public static List<Document> getDocuments() {
        return new Vector<>(documents.values());
    }

    public static List<Abonne> getSubscribers() {
        return new Vector<>(subscribers.values());
    }

    public static List<DocumentLogEntity> getDocumentLogs() {
        return new Vector<>(documentLogs.values());
    }
}
