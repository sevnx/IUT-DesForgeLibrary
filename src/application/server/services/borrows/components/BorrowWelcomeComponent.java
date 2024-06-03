package application.server.services.borrows.components;

import librairies.server.Component;
import librairies.server.Service;

public class BorrowWelcomeComponent implements Component {
    @Override
    public void call(Service service) {
        StringBuilder sb = new StringBuilder();

        sb.append("----------[ SERVICE D'EMPRUNT ]----------")
          .append(System.lineSeparator())
          .append("Bienvenue au service d'emprunt de la médiathèque. Voici le catalogue des ouvrages disponibles :")
          .append(System.lineSeparator());

        sb.append("") // TODO: call service to get catalog
          .append(System.lineSeparator())
          .append("Pour emprunter un ouvrage, veuillez saisir votre numéro d'abonné :");

        service.send(sb.toString());
    }
}
