package server.media_library.interfaces;

public abstract class RetourException extends DocumentException {
    public RetourException(String message) {
        super(message);
    }
}
