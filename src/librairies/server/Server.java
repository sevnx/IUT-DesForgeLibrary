package librairies.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.reflect.InvocationTargetException;

public class Server implements Runnable {
    private final int port;
    private final String name;
    private final ServerSocket listenSocket;
    private final Class<? extends Service> serviceClass;
    private Service actualServiceClass;

    public Server(Class<? extends Service> serviceClass, int port, String name) throws IOException {
        this.serviceClass = serviceClass;
        this.listenSocket = new ServerSocket(port);
        this.actualServiceClass = null;
        this.port = port;
        this.name = name;
    }

    public Server(Class<? extends Service> serviceClass, int port) throws IOException {
        this(serviceClass, port, "Not defined");
    }

    public String getName() {
        return this.name;
    }
    
    // TODO: Implement getIp method

    public ServerSocket getListenSocket() {
        return listenSocket;
    }

    // TODO: Refactor this method
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
