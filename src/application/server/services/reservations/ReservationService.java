package application.server.services.reservations;

import librairies.server.Service;

import java.io.IOException;
import java.net.Socket;

public class ReservationService extends Service {
    public ReservationService(Socket socket) throws IOException {
        super(socket, ReservationDocumentComponent.class);
    }
}
