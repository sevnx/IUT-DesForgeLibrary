package application.server.factories;

import application.server.domain.core.Entity;
import application.server.managers.DataManager;
import application.server.models.DocumentLogModel;
import application.server.models.DocumentModel;
import application.server.models.DvdModel;
import application.server.models.SubscriberModel;

import java.sql.SQLException;
import java.util.Vector;

public class DataFactory {
    public static void populateData() {
        try {
            populate(new SubscriberModel().get());
            populate(new DocumentModel().get());
            populate(new DvdModel().get());
            populate(new DocumentLogModel().get());
        } catch (SQLException ignored) {
        }
    }

    private static void populate(Vector<? extends Entity> models) {
        for (Entity model : models) {
            DataManager.addEntity(model);
        }
    }
}
