package application.client;

import application.client.factory.ClientFactory;
import application.client.manager.ClientManager;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class ClientStart {
    public static void main(String[] args) {
        try {
            ClientManager clientManager = ClientFactory.create(args);
            ClientFactory.manage(clientManager);
            ClientFactory.close(clientManager);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | IOException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
