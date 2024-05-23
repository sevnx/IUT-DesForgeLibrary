package server.medialibrary.interfaces;

public abstract class BorrowException extends DocumentException {
    public BorrowException(String message) {
        super(message);
    }
}
