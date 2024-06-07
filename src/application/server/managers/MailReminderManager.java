package application.server.managers;

import application.server.domain.entities.interfaces.Abonne;
import application.server.domain.entities.interfaces.Document;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manager for the mail reminders.
 * Mail reminders are sent to subscribers when a document is available for them.
 * The reminders are stored in a map with the document as the key and the list of subscribers as the value.
 */
public class MailReminderManager {
    private static final Logger LOGGER = LogManager.getLogger("Mail Reminder Manager");
    private static final ConcurrentHashMap<Integer, List<Integer>> reminders = new ConcurrentHashMap<>();

    public static void addReminder(Document document, Abonne abonne) {
        LOGGER.debug("Adding reminder for document {} and subscriber {}", document.numero(), abonne.getId());

        if (!reminders.containsKey(document.numero())) {
            reminders.put(document.numero(), new ArrayList<>());
        }
        reminders.get(document.numero()).add(abonne.getId());
        LOGGER.debug(reminders.get(document.numero()));
        LOGGER.debug("Reminder added for document {} and subscriber {}", document.numero(), abonne.getId());
    }

    public static void sendReminder(Document document) {
        LOGGER.debug("Sending reminder for document {}", document.numero());
        List<Integer> abonnes = reminders.get(document.numero());
        if (abonnes == null) {
            LOGGER.warn("No reminder found for document {}", document.numero());
            return;
        }
        for (Integer abonne : abonnes) {
            Abonne abonneEntity = DataManager.getSubscriber(abonne).orElseThrow();
            LOGGER.debug("Sending reminder to subscriber {}", abonne);
            try {
                EmailManager.sendEmail(abonneEntity.getEmail(), "Document disponible",
                        "Bonjour " + abonneEntity.getNom() + "," + System.lineSeparator() +
                                "La tribu est heurese de vous informer que le document " + document + " est de nouveau disponible en Médiathèque.\n" +
                                "Cordialement,\n" +
                                "La médiathèque DesForge");
            } catch (Exception e) {
                LOGGER.error("Error sending reminder to subscriber {}", abonne, e);
            }
        }
        clearReminders(document);
    }

    private static void clearReminders(Document document) {
        LOGGER.debug("Clearing reminders for document {}", document.numero());
        reminders.remove(document);
    }
}
