package application.server.services.borrows;

import librairies.server.Server;

import java.io.IOException;

public class BorrowServer extends Server {
    private static final int SERVICE_PORT = 4000;
    private static final String SERVICE_NAME = "Borrow";
    private static final Class<BorrowService> SERVICE_CLASS = BorrowService.class;

    public BorrowServer() throws IOException {
        super(SERVICE_CLASS, SERVICE_PORT, SERVICE_NAME);
    }
}
