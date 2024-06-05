package application.client.factory;

import application.client.manager.ClientManager;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class ClientFactory {
    private static final int DEFAULT_CLIENT_PORT = 4000;
    private static final String DEFAULT_CLIENT_HOST = "localhost";

    /**
     * Creates and returns a ClientManager based on the provided arguments
     *
     * @param args array of arguments for host and port
     * @return ClientManager configured with provided or default values
     * @throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException, NoSuchMethodException
     */
    public static ClientManager create(String[] args) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException, NoSuchMethodException {
        ClientManager clientManager = switch (args.length) {
            case 1 -> new ClientManager(DEFAULT_CLIENT_HOST, Integer.parseInt(args[0]));
            case 2 -> new ClientManager(args[0], Integer.parseInt(args[1]));
            default -> new ClientManager(DEFAULT_CLIENT_HOST, DEFAULT_CLIENT_PORT);
        };

        System.out.println(clientManager.read());

        return clientManager;
    }

    /**
     * Manages client communication
     *
     * @param clientManager the ClientManager instance to manage
     * @throws IOException
     */
    public static void manage(ClientManager clientManager) throws IOException {
        boolean sendMessage = false;

        while (true) {
            clientManager.send(sendMessage ? "" : clientManager.getInputStream().readLine());
            sendMessage = false;
            String response = clientManager.read();

            if ("exit".equals(response)) break;

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
     * @throws IOException
     */
    public static void close(ClientManager clientManager) throws IOException {
        clientManager.close();
    }
}
