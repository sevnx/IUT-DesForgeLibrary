package application.server.services.returns;

import librairies.server.Server;

import java.io.IOException;

public class ReturnServer extends Server {
    private static final int SERVICE_PORT = 5000;
    private static final String SERVICE_NAME = "Return";
    private static final Class<ReturnService> SERVICE_CLASS = ReturnService.class;

    public ReturnServer() throws IOException {
        super(SERVICE_CLASS, SERVICE_PORT, SERVICE_NAME);
    }
}
