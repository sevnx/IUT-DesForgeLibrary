package application.server.services.reservations;

import application.server.domain.entities.interfaces.Abonne;
import application.server.domain.entities.interfaces.Document;
import application.server.domain.entities.types.ReservationNotAvailableException;
import application.server.managers.DataManager;
import application.server.managers.MailReminderManager;
import application.server.services.ServiceUtils;
import librairies.server.Component;
import librairies.server.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReservationDocumentComponent implements Component {
    private static final Logger LOGGER = LogManager.getLogger("Document reservation service");

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
                    "Veuillez saisir le numéro de l'ouvrage que vous souhaitez réserver :";
            service.send(askForDocumentIdMessage);

            // Get the document id
            Integer idDocument = null;
            while (idDocument == null || DataManager.getDocument(idDocument).isEmpty()) {
                idDocument = ServiceUtils.askId(service);
            }
            Document document = DataManager.getDocument(idDocument).get();

            // Process the reservation
            StringBuilder reservationResponse = new StringBuilder();
            try {
                document.reservation(subscriber);
                reservationResponse.append("Réservation effectuée avec succès").append(System.lineSeparator());
            } catch (ReservationNotAvailableException e) {
                reservationResponse.append(e.getMessage()).append(System.lineSeparator());
                boolean sendEmail = ServiceUtils.askBoolean(service, "Voulez-vous être notifié par email lorsque le document sera disponible ? (o/n)");
                if (sendEmail) {
                    MailReminderManager.addReminder(document, subscriber);
                }
                reservationResponse = new StringBuilder();
            } catch (Exception e) {
                reservationResponse.append(e.getMessage()).append(System.lineSeparator());
            } finally {
                reservationResponse.append("Voulez-vous réserver un autre document ? (o/n)");
                if (ServiceUtils.askBoolean(service, reservationResponse.toString())) {
                    call(service);
                } else {
                    String goodbyeMessage =
                            "Au revoir !" + System.lineSeparator() +
                                    "Merci de votre visite." + System.lineSeparator() +
                                    "[EXIT]";
                    service.send(goodbyeMessage);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error reserving document", e);
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
        sb.append("Bienvenue dans le service de réservation de documents.").append(System.lineSeparator());

        sb.append("Voici le catalogue des documents disponibles :").append(System.lineSeparator());
        DataManager.getDocuments().forEach(document -> sb.append(document).append(System.lineSeparator()));

        return sb;
    }
}
