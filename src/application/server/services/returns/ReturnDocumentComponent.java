package application.server.services.returns;

import application.server.domain.entities.interfaces.Document;
import application.server.managers.DataManager;
import application.server.services.ServiceUtils;
import librairies.server.Component;
import librairies.server.Service;

public class ReturnDocumentComponent implements Component {
    @Override
    public void call(Service service) {
        try {
            StringBuilder askForDocumentIdMessage = getWelcomeMessage();

            // Ask for the document ID
            askForDocumentIdMessage.append("Veuillez entrer l'identifiant du document que vous souhaitez retourner :");
            service.send(askForDocumentIdMessage.toString());

            // Get the document ID
            Integer idDocument = null;
            while (idDocument == null || DataManager.getDocument(idDocument).isEmpty()) {
                idDocument = ServiceUtils.askId(service);
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
            e.printStackTrace();
        }
    }

    private StringBuilder getWelcomeMessage() {
        StringBuilder welcomeMessage = new StringBuilder();
        welcomeMessage.append("Bienvenue sur le service de retour de documents.").append(System.lineSeparator());
        return welcomeMessage;
    }
}
