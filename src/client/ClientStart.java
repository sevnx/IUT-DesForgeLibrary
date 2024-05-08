package client;

import java.util.List;

import static java.lang.Integer.parseInt;

public class ClientStart {
    private static final List<Integer> PORTS = List.of(3000, 4000, 5000);

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Please provide a port number.");
            return;
        }

        int port = parseInt(args[0]);
        if(!PORTS.contains(port)) {
            try {
                new Thread(new ClientProtocol("localhost", port)).start();
                System.out.println("Client started on port " + port + ". If you want to stop the client, please type 'exit'.");
            } catch (Exception e) {
                System.err.println("The server you are trying to connect to is not available.");
            }
        } else {
            System.err.println("Port number is not valid.");
        }
    }
}
