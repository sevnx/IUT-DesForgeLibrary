package application.server.domain.entities.interfaces;

public abstract class DocumentException extends RuntimeException {
    public DocumentException(String message) {
        super(message);
    }
}
