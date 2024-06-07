package application.server.factories;

import application.server.configuration.ServerConfig;
import application.server.managers.ConfigurationManager;
import application.server.managers.ServerManager;
import application.server.services.borrows.BorrowServer;
import application.server.services.reservations.ReservationServer;
import application.server.services.returns.ReturnServer;
import librairies.server.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Vector;

public class ServerFactory {
    private static final Logger LOGGER = LogManager.getLogger("Server Factory");
    private static final Vector<Server> launchedServers = new Vector<>();

    public static void setupPorts() {
        LOGGER.info("Setting up server ports (reading from configuration)");
        ServerConfig serverConfig = ConfigurationManager.getServerConfig();

        BorrowServer.setServicePort(serverConfig.documentBorrowServer().port());
        ReservationServer.setServicePort(serverConfig.documentReservationServer().port());
        ReturnServer.setServicePort(serverConfig.documentReturnServer().port());
        LOGGER.info("Server ports set");
    }

    public static void launch() throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        LOGGER.info("Launching servers");
        ArrayList<Class<? extends Server>> serverList = new ArrayList<>();

        serverList.add(BorrowServer.class);
        serverList.add(ReservationServer.class);
        serverList.add(ReturnServer.class);

        for (Class<? extends Server> serverClass : serverList) {
            Server server = ServerManager.start(serverClass);
            launchedServers.add(server);
        }
        LOGGER.info("Servers launched");
    }

    public static void close() throws IOException {
        for (Server server : launchedServers) {
            ServerManager.stop(server);
        }
    }
}
