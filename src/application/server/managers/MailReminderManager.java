package application.server.managers;

import application.server.entities.Abonne;
import application.server.entities.Document;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
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
            reminders.put(document.numero(), new Vector<>());
        }
        reminders.get(document.numero()).add(abonne.getId());
        LOGGER.debug("Reminder added for document {} and subscriber {}", document.numero(), abonne.getId());
    }

    public static void sendReminder(Document document) {
        LOGGER.info("Sending reminder for document {}", document.numero());
        List<Integer> abonnes = reminders.get(document.numero());
        if (abonnes == null) {
            LOGGER.warn("No reminder found for document {}", document.numero());
            return;
        }
        for (Integer abonne : abonnes) {
            Document documentEntity = DataManager.getDocument(document.numero()).orElseThrow();
            Abonne abonneEntity = DataManager.getSubscriber(abonne).orElseThrow();
            LOGGER.debug("Sending reminder to subscriber {}", abonne);
            try {
                EmailManager.sendEmail(abonneEntity.getEmail(), "Médiathèque DesForge - Document disponible - CORENTIN Lenclos (209) / CZYKINOWSKI Seweryn (207)",
                        String.format(
                                """
                                         🏹🌟 Bonne nouvelle de la tribu de votre Médiathèque ! 🌟🏹
                                        \s
                                         %s, je suis ravi de t'annoncer que le document %s est de nouveau disponible ! 📚✨
                                         Nos fiers guerriers des rayonnages ont veillé jour et nuit pour s'assurer que tu puisses retrouver ce trésor de connaissances. 🌌🔥
                                                                           \s
                                         C'est un peu comme si un membre de notre tribu avait rapporté ce document à travers les vastes plaines de notre médiathèque, pour finalement le remettre à sa place. 🏞️🦅
                                                                           \s
                                         En tant qu'honorable membre de notre communauté, il est temps pour toi de reprendre ta quête de savoir et d'explorer les mystères et les récits fascinants de cette tribu légendaire. 🌟📖
                                                                           \s
                                         Viens vite à la médiathèque pour récupérer ton précieux butin ! Nos chamans de l'accueil seront ravis de te le remettre en mains propres et de te voir plonger dans cette aventure épique. 🎉🪶
                                                                           \s
                                         Si tu as des questions, n'hésite pas à nous envoyer un signe de fumée, ou tout simplement à répondre à cet email. 📬🔥
                                         À très bientôt, noble chasseur de savoirs !
                                                                           \s
                                         Avec l'esprit de la tribu,
                                         Médiathèque DesForge 🌟🏹
                                        \s""",
                                abonneEntity.getNom(),
                                documentEntity
                        )
                );
            } catch (Exception e) {
                LOGGER.error("Error sending reminder to subscriber {}", abonne, e);
            }
        }
        clearReminders(document);
    }

    private static void clearReminders(Document document) {
        LOGGER.debug("Clearing reminders for document {}", document.numero());
        reminders.remove(document.numero());
    }
}
