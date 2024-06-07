package application.server.managers;

import librairies.server.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class ServerManager {
    private static final Logger LOGGER = LogManager.getLogger("Server Manager");

    public static Server start(Class<? extends Server> serverStart) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Server server = serverStart.getConstructor().newInstance();
        LOGGER.info("Starting server {} on port {}", server.getName(), server.getIp());
        server.start();
        return server;
    }

    public static void stop(Server serverStop) throws IOException {
        serverStop.stop();
        LOGGER.info("Stopping server {} on port {}", serverStop.getName(), serverStop.getIp());
    }
}
