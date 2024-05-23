package server.medialibrary.interfaces;

public abstract class ReturnException extends DocumentException {
    public ReturnException(String message) {
        super(message);
    }
}
