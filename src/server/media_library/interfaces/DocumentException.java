package server.media_library.interfaces;

public abstract class DocumentException extends RuntimeException {
    public DocumentException(String message) {
        super(message);
    }
}
