package application.server.services.borrows;

import application.server.domain.entities.interfaces.Abonne;
import application.server.domain.entities.interfaces.Document;
import application.server.managers.DataManager;
import application.server.services.ServiceUtils;
import librairies.server.Component;
import librairies.server.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BorrowDocumentComponent implements Component {
    private static final Logger LOGGER = LogManager.getLogger("Document borrow service");

    @Override
    public void call(Service service) {
        try {
            StringBuilder askForSubscriberIdMessage = getWelcomeMessage();

            // Ask for the subscriber's id
            askForSubscriberIdMessage.append("Veuillez saisir votre numéro d'abonné :");
            service.send(askForSubscriberIdMessage.toString());

            // Get the subscriber id
            Integer idSubscriber = null;
            while (idSubscriber == null || DataManager.getSubscriber(idSubscriber).isEmpty()) {
                idSubscriber = ServiceUtils.askId(service);
            }
            Abonne subscriber = DataManager.getSubscriber(idSubscriber).get();

            // Ask for the document id
            String askForDocumentIdMessage = "Bonjour " + subscriber.getNom() + System.lineSeparator() +
                    "Veuillez saisir le numéro de l'ouvrage que vous souhaitez emprunter :";
            service.send(askForDocumentIdMessage);

            // Get the document id
            Integer idDocument = null;
            while (idDocument == null || DataManager.getDocument(idDocument).isEmpty()) {
                idDocument = ServiceUtils.askId(service);
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
