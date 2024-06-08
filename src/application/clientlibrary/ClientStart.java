package application.clientlibrary;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Main entry point for the client application for the library.
 * This application handles :
 * - Borrowing
 * - Returning
 */
public class ClientStart {
    public static void main(String[] args) {
        try {
            ClientManager clientManager = ClientFactory.create(args);
            ClientFactory.manage(clientManager);
            ClientFactory.close(clientManager);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | IOException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
