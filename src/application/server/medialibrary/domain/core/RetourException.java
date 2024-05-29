package application.server.medialibrary.domain.core;

public abstract class RetourException extends DocumentException {
    public RetourException(String message) {
        super(message);
    }
}
