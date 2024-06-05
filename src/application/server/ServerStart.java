package application.server;

import application.server.domain.core.Entity;
import application.server.factories.DataFactory;
import application.server.factories.DatabaseFactory;
import application.server.factories.ServerFactory;
import application.server.managers.ConfigurationManager;
import application.server.managers.DataManager;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class ServerStart {
    public static void main(String[] args) {
        try {
            DatabaseFactory.setupDatabase();
            ServerFactory.launch();

            DataFactory.populateData();
            DataManager.getEntities().stream().map(Entity::getIdentifier).forEach(System.out::println);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
