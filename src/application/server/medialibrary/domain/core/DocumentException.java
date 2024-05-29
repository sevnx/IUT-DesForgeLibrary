package application.server.medialibrary.domain.core;

public abstract class DocumentException extends RuntimeException {
    public DocumentException(String message) {
        super(message);
    }
}
