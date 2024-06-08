package application.clientlibrary;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Client factory to create and manage clients for the library
 */
public class ClientFactory {
    private static final String DEFAULT_CLIENT_HOST = "localhost";

    /**
     * Creates and returns a ClientManager based on the provided arguments
     *
     * @param args array of arguments for host and port
     * @return ClientManager configured with provided and default values
     */
    public static ClientManager create(String[] args) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException, NoSuchMethodException {
        ClientManager clientManager = new ClientManager(DEFAULT_CLIENT_HOST, Integer.parseInt(args[0]));
        System.out.println(clientManager.read());
        return clientManager;
    }

    /**
     * Manages clientuser communication
     *
     * @param clientManager the ClientManager instance to manage
     */
    public static void manage(ClientManager clientManager) throws IOException {
        boolean sendMessage = false;

        while (true) {
            clientManager.send(sendMessage ? "" : clientManager.getInputStream().readLine());
            sendMessage = false;
            String response = clientManager.read();

            if (response.endsWith("[EXIT]")) {
                System.out.println(response.replace("[EXIT]", "").trim());
                break;
            }

            if (response.endsWith("[SEND]")) {
                response = response.replace("[SEND]", "").trim();
                sendMessage = true;
            }
            System.out.println(response);
        }
    }

    /**
     * Closes the ClientManager instance
     *
     * @param clientManager the ClientManager instance to close
     */
    public static void close(ClientManager clientManager) throws IOException {
        clientManager.close();
    }
}
