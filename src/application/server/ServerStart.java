package application.server;

import application.server.factories.ServerFactory;

import java.lang.reflect.InvocationTargetException;

public class ServerStart {
    public static void main(String[] args) {
        try {
            ServerFactory.launch();
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
