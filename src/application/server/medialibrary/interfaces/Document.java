package application.server.medialibrary.interfaces;

import application.server.medialibrary.Abonne;

public interface Document {
    int numero();

    void reservation(Abonne ab) throws ReservationException;
    void emprunt(Abonne ab) throws BorrowException;
    void retour() throws ReturnException;
}
