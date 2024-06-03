package application.server.services.reservations.components;

import librairies.server.Component;
import librairies.server.Service;

public class ReservationWelcomeComponent implements Component {
    @Override
    public void call(Service service) {
        StringBuilder sb = new StringBuilder();

        sb.append("----------[ SERVICE DE RÉSERVATION ]----------")
          .append(System.lineSeparator())
          .append("Bienvenue au service de réservation de la médiathèque. Voici le catalogue des ouvrages disponibles :")
          .append(System.lineSeparator());

        sb.append("") // TODO: call service to get catalog
          .append(System.lineSeparator())
          .append("Pour emprunter un ouvrage, veuillez saisir votre numéro d'abonné :");

        service.send(sb.toString());
    }
}
