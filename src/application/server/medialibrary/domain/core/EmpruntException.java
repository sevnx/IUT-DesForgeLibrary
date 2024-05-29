package application.server.medialibrary.domain.core;

public abstract class EmpruntException extends DocumentException {
    public EmpruntException(String message) {
        super(message);
    }
}
