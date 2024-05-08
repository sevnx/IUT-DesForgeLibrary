package client;

import librairies.protocol.CodeDecode;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientProtocol implements Runnable {
    private final Socket clientSocket;
    private final BufferedReader input;
    private final BufferedReader sIn;
    private final PrintWriter sOut;

    public ClientProtocol(String host, int port) throws IOException {
        clientSocket = new Socket(host, port);
        input = new BufferedReader(new InputStreamReader(System.in));
        sIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        sOut = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    private void read() throws IOException {
        System.out.println(CodeDecode.decode(sIn.readLine()));
    }

    private void send() throws IOException {
        String clientInput = input.readLine();
        sOut.println(CodeDecode.code(clientInput));
    }

    @Override
    public void run() {
        try {
            while (true) {
                read();
                send();
            }
        } catch (IOException e) {
            System.out.println("define a message for the exception");
        }
    }
}
