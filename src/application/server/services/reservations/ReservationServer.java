package application.server.services.reservations;

import librairies.server.Server;

import java.io.IOException;

public class ReservationServer extends Server {
    private static final int SERVICE_PORT = 3000;
    private static final String SERVICE_NAME = "Reservation";
    private static final Class<ReservationService> SERVICE_CLASS = ReservationService.class;

    public ReservationServer() throws IOException {
        super(SERVICE_CLASS, SERVICE_PORT, SERVICE_NAME);
    }
}
