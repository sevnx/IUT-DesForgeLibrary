package application.server.services.borrows;

import librairies.server.Service;

import java.io.IOException;
import java.net.Socket;

public class BorrowService extends Service {
    public BorrowService(Socket socket) throws IOException {
        super(socket, BorrowDocumentComponent.class);
    }
}
