package application.server;

import application.server.factories.DataFactory;
import application.server.factories.DatabaseFactory;
import application.server.factories.ServerFactory;
import application.server.factories.TimerFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

import java.lang.reflect.InvocationTargetException;

public class ServerStart {
    public static void main(String[] args) {
        try {
            Configurator.setRootLevel(Level.INFO);
            DatabaseFactory.setupDatabase();
            DataFactory.populateData();
            TimerFactory.setupTimers();

            ServerFactory.setupPorts();
            ServerFactory.launch();
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
