package application.server.factories;

import application.server.configs.EmailConfig;
import application.server.managers.ConfigurationManager;
import application.server.managers.EmailManager;

/**
 * EmailFactory class
 * Loads the email configs and sets up the email sender manager
 */
public class EmailFactory {
    public static void setupEmail() {
        EmailConfig emailConfig = ConfigurationManager.getConfig(EmailConfig.class, EmailConfig.getEmailConfigFilePath());
        EmailManager.setFromConfig(emailConfig);
    }
}