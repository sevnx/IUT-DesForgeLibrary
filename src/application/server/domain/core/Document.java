package application.server.domain.core;

public interface Document {
    int numero();

    void reservation(Abonne ab) throws ReservationException;

    void emprunt(Abonne ab) throws EmpruntException;

    void retour() throws RetourException;
}
