package application.server.services.borrows.components;

import application.server.domain.entities.interfaces.Document;
import application.server.domain.entities.types.DvdEntity;
import application.server.managers.DataManager;
import librairies.server.Component;
import librairies.server.Service;

import java.util.Comparator;

public class BorrowWelcomeComponent implements Component {
    @Override
    public void call(Service service) {
        StringBuilder sb = new StringBuilder();

        sb.append("----------[ SERVICE D'EMPRUNT ]----------")
          .append(System.lineSeparator())
          .append("Bienvenue au service d'emprunt de la médiathèque. Voici le catalogue des ouvrages disponibles :")
          .append(System.lineSeparator());

        DataManager.getDocuments().forEach(document -> sb.append(document.numero())
          .append(" - ")
          .append(document)
          .append(System.lineSeparator()));

        sb.append("") // TODO: call service to get catalog
          .append(System.lineSeparator())
          .append("Pour emprunter un ouvrage, veuillez saisir votre numéro d'abonné :");

        service.send(sb.toString());
    }
}
