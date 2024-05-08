package librairies.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.reflect.InvocationTargetException;

public class Server implements Runnable {
    private final Class<? extends Runnable> service;
    private final ServerSocket listenSocket;

    public Server(Class<? extends Runnable> service, int port) throws IOException {
        this.service = service;
        this.listenSocket = new ServerSocket(port);
    }

    public void run() {
        while (!this.listenSocket.isClosed()) {
            try {
                new Thread(this.service.getConstructor(Socket.class).newInstance(this.listenSocket.accept())).start();
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException | IOException | NoSuchMethodException e) {
                System.err.println("Error opening server: " + e.getMessage());
            }
        }
    }
}
