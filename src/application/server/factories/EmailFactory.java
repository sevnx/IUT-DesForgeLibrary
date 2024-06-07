package application.server.factories;

import application.server.managers.ConfigurationManager;
import application.server.managers.EmailManager;
import application.server.configuration.EmailConfig;

public class EmailFactory {
    public static void setupEmail() {
        EmailConfig emailConfig = ConfigurationManager.getConfig(EmailConfig.class, EmailConfig.getEmailConfigFilePath());
        EmailManager.setFromConfig(emailConfig);
    }
}