package application.server.services.returns.components;

import librairies.server.Component;
import librairies.server.Service;

public class ReturnWelcomeComponent implements Component {
    @Override
    public void call(Service service) {
        StringBuilder sb = new StringBuilder();

        sb.append("----------[ SERVICE DE RETOUR ]----------")
          .append(System.lineSeparator())
          .append("Bienvenue au service de retour de la médiathèque. Voici le catalogue des ouvrages empruntés :")
          .append(System.lineSeparator());

        sb.append("") // TODO: call service to get catalog
          .append(System.lineSeparator())
          .append("Veuillez saisir le numéro de l'ouvrage à retourner :");

        service.send(sb.toString());
    }
}
