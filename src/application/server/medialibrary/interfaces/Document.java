package server.medialibrary.interfaces;

import server.medialibrary.Abonne;

public interface Document {
    int numero();

    void reservation(Abonne ab) throws ReservationException;
    void emprunt(Abonne ab) throws EmpruntException;
    void retour() throws RetourException;
}
