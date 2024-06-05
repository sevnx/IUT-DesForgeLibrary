package application.server;

import application.server.factories.DataFactory;
import application.server.factories.DatabaseFactory;
import application.server.factories.ServerFactory;
import application.server.managers.DataManager;

import java.lang.reflect.InvocationTargetException;

public class ServerStart {
    public static void main(String[] args) {
        try {
            DatabaseFactory.setupDatabase();
            ServerFactory.launch();
            DataFactory.populateData();
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
