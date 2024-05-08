package librairies.server;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.PrintWriter;

public abstract class Service implements Runnable {
    private final Socket client;
    private final BufferedReader in;
    private final PrintWriter out;

    public Service(Socket socket) throws IOException {
        this.client = socket;
        this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
        this.out = new PrintWriter(this.client.getOutputStream(), true);
    }

    // TODO: Implement run method in ServiceEmprunt + ServiceReservation + ServiceRetour
    protected Socket getClient() {
        return this.client;
    }

    // TODO: Implement run method in ServiceEmprunt + ServiceReservation + ServiceRetour
    protected BufferedReader getIn() {
        return this.in;
    }

    // TODO: Implement run method in ServiceEmprunt + ServiceReservation + ServiceRetour
    protected PrintWriter getOut() {
        return this.out;
    }

    // TODO: Define a checkSubscriber method (checkDocument is already defined)
}
