package application.server.services.reservations;

import application.server.services.reservations.components.ReservationDocumentComponent;
import application.server.services.reservations.components.ReservationWelcomeComponent;
import librairies.server.Component;
import librairies.server.Service;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ReservationService extends Service {
    public ReservationService(Socket socket) throws IOException {
        super(socket);
    }

    @Override
    protected List<Class<? extends Component>> componentList() {
        ArrayList<Class<? extends Component>> components = new ArrayList<>();

        components.add(ReservationWelcomeComponent.class);
        components.add(ReservationDocumentComponent.class);

        return components;
    }
}
