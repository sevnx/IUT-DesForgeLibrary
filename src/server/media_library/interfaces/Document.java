package server.media_library.interfaces;

import server.media_library.Abonne;

public interface Document {
    int numero();

    void reservation(Abonne ab) throws ReservationException;
    void emprunt(Abonne ab) throws EmpruntException;
    void retour() throws RetourException;
}
