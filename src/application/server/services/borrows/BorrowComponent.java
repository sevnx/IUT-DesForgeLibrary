package application.server.services.borrows;

import application.server.entities.Abonne;
import application.server.entities.Document;
import application.server.managers.DataManager;
import application.server.services.ServiceUtils;
import libraries.server.Component;
import libraries.server.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BorrowComponent implements Component {
    private static final Logger LOGGER = LogManager.getLogger("Document borrow service");

    @Override
    public void call(Service service) {
        try {
            StringBuilder askForSubscriberIdMessage = getWelcomeMessage();

            // Ask for the subscriber's id
            askForSubscriberIdMessage.append("Veuillez saisir votre numéro d'abonné :");
            service.send(askForSubscriberIdMessage.toString());

            // Get the subscriber id
            int idSubscriber;
            while (true) {
                idSubscriber = ServiceUtils.askId(service);
                if (DataManager.getSubscriber(idSubscriber).isEmpty()) {
                    service.send("Entrée incorrecte, veuillez réessayer." + System.lineSeparator() + "Veuillez saisir votre numéro d'abonné :");
                } else {
                    break;
                }
            }
            Abonne subscriber = DataManager.getSubscriber(idSubscriber).get();

            // Ask for the document id
            String askForDocumentIdMessage = "Bonjour " + subscriber.getNom() + System.lineSeparator() +
                    "Veuillez saisir le numéro de l'ouvrage que vous souhaitez emprunter :";
            service.send(askForDocumentIdMessage);

            // Get the document id
            int idDocument;
            while (true) {
                idDocument = ServiceUtils.askId(service);
                if (DataManager.getDocument(idDocument).isEmpty()) {
                    service.send("Entrée incorrecte, veuillez réessayer." + System.lineSeparator() + "Veuillez saisir le numéro de l'ouvrage que vous souhaitez emprunter :");
                } else {
                    break;
                }
            }
            Document document = DataManager.getDocument(idDocument).get();

            // Process the borrowing
            StringBuilder borrowResponse = new StringBuilder();
            try {
                document.emprunt(subscriber);
                borrowResponse.append("Emprunt effectué avec succès").append(System.lineSeparator());
            } catch (Exception e) {
                borrowResponse.append(e.getMessage()).append(System.lineSeparator());
            } finally {
                borrowResponse.append("Voulez-vous emprunter un autre document ? (o/n)");
                if (ServiceUtils.askBoolean(service, borrowResponse.toString())) {
                    call(service);
                } else {
                    String goodbyeMessage = "Au revoir !" + System.lineSeparator() +
                            "Merci de votre visite." + System.lineSeparator() +
                            "[EXIT]";
                    service.send(goodbyeMessage);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error borrowing document", e);
            service.send("Une erreur est survenue, veuillez réessayer.");
        }
    }

    /**
     * Gets the welcome message
     *
     * @return the welcome message StringBuilder as BTTP follows a request-response pattern
     */
    private StringBuilder getWelcomeMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Bienvenue dans le service d'emprunt de documents.").append(System.lineSeparator());
        return sb;
    }
}
