package application.server.services.returns;

import librairies.server.Service;

import java.io.IOException;
import java.net.Socket;

public class ReturnService extends Service {
    public ReturnService(Socket socket) throws IOException {
        super(socket, ReturnDocumentComponent.class);
    }
}
