package server.medialibrary.interfaces;

public abstract class DocumentException extends RuntimeException {
    public DocumentException(String message) {
        super(message);
    }
}
