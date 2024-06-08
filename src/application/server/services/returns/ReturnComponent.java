package application.server.services.returns;

import application.server.entities.Document;
import application.server.managers.DataManager;
import application.server.services.ServiceUtils;
import libraries.server.Component;
import libraries.server.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReturnComponent implements Component {
    private static final Logger LOGGER = LogManager.getLogger("Return Document Component");

    @Override
    public void call(Service service) {
        try {
            StringBuilder askForDocumentIdMessage = getWelcomeMessage();

            // Ask for the document ID
            askForDocumentIdMessage.append("Veuillez entrer l'identifiant du document que vous souhaitez retourner :");
            service.send(askForDocumentIdMessage.toString());

            // Get the document ID
            int idDocument;
            while (true) {
                idDocument = ServiceUtils.askId(service);
                if (DataManager.getDocument(idDocument).isEmpty()) {
                    service.send("Entrée incorrecte, veuillez réessayer." + System.lineSeparator() +
                            "Veuillez entrer l'identifiant du document que vous souhaitez retourner :");
                } else {
                    break;
                }
            }
            Document document = DataManager.getDocument(idDocument).get();

            // Process the return
            StringBuilder returnResponse = new StringBuilder();

            try {
                document.retour();
                returnResponse.append("Retour effectué avec succès").append(System.lineSeparator());
            } catch (Exception e) {
                returnResponse.append(e.getMessage()).append(System.lineSeparator());
            } finally {
                returnResponse.append("Voulez-vous retourner un autre document ? (o/n)");
                if (ServiceUtils.askBoolean(service, returnResponse.toString())) {
                    call(service);
                } else {
                    String goodbyeMessage = "Au revoir !" + System.lineSeparator() +
                            "Merci de votre visite." + System.lineSeparator() +
                            "[EXIT]";
                    service.send(goodbyeMessage);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error while returning a document: {}", e.getMessage());
        }
    }

    private StringBuilder getWelcomeMessage() {
        StringBuilder welcomeMessage = new StringBuilder();
        welcomeMessage.append("Bienvenue sur le service de retour de documents.").append(System.lineSeparator());
        return welcomeMessage;
    }
}
