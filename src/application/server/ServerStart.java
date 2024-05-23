package server;

import librairies.server.Server;
import server.services.ServiceReservation;
import server.services.ServiceEmprunt;
import server.services.ServiceRetour;

import java.lang.reflect.InvocationTargetException;
import java.io.IOException;

public class ServerStart {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException, NoSuchMethodException {
        new Thread(new Server(ServiceReservation.class, 3000)).start();
        new Thread(new Server(ServiceEmprunt.class, 4000)).start();
        new Thread(new Server(ServiceRetour.class, 5000)).start();
    }
}
