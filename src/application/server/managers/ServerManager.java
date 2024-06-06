package application.server.managers;

import librairies.server.Server;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class ServerManager {
    public static Server start(Class<? extends Server> serverStart) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException, InvocationTargetException {
        Server server = serverStart.getConstructor().newInstance();

        server.start();
        System.out.println("Starting server \"" + server.getName() + "\" on port " + server.getIp());

        return server;
    }

    public static void stop(Server serverStop) throws IOException {
        serverStop.stop();
        System.out.println("Stopping server \"" + serverStop.getName() + "\" on port " + serverStop.getIp());
    }
}
