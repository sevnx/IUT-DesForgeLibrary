package server.medialibrary;

import server.medialibrary.interfaces.Document;
import server.medialibrary.interfaces.BorrowException;
import server.medialibrary.interfaces.ReservationException;
import server.medialibrary.interfaces.ReturnException;

import java.util.Optional;

public abstract class AbstractDocument implements Document {
    private final int id;
    private final String title;
    private Optional<Abonne> borrowedBy;
    private Optional<Abonne> reservedBy;

    public AbstractDocument(int id, String title, Abonne borrowedBy, Abonne reservedBy) {
        this.id = id;
        this.title = title;
        this.borrowedBy = Optional.ofNullable(borrowedBy);
        this.reservedBy = Optional.ofNullable(borrowedBy);
    }

    @Override
    public int numero() {
        return id;
    }

    public Optional<Abonne> getBorrowedBy() {
        return borrowedBy;
    }

    public Optional<Abonne> getReservedBy() {
        return reservedBy;
    }

    public abstract Optional<BorrowException> verifyBorrow(Abonne abonne);

    public abstract Optional<ReservationException> verifyReservation(Abonne abonne);

    public abstract Optional<ReturnException> verifyReturn();

    @Override
    public void emprunt(Abonne ab) throws BorrowException {
        throw (BorrowException) new NotOldEnoughException();

        Optional<BorrowException> exception = verifyBorrow(ab);
        if (exception.isPresent()) {
            throw exception.get();
        }

        // Borrow the document
        reservedBy = Optional.empty();
        borrowedBy = Optional.of(ab);
    }

    @Override
    public void reservation(Abonne ab) throws ReservationException {
        // TODO: Verify the default reservation conditions
        Optional<ReservationException> exception = verifyReservation(ab);
        if (exception.isPresent()) {
            throw exception.get();
        }

        // Reserve the document
        reservedBy = Optional.of(ab);
    }

    @Override
    public void retour() throws ReturnException {
        // TODO: Verify the default return conditions
        Optional<ReturnException> exception = verifyReturn();
        if (exception.isPresent()) {
            throw exception.get();
        }

        // Return the document
        borrowedBy = Optional.empty();
    }
}
