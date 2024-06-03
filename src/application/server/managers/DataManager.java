package application.server.managers;

import application.server.domain.core.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DataManager {
    private static final ConcurrentHashMap<String, Entity> entityMap = new ConcurrentHashMap<>();

    public static void addEntity(Entity entity) {
        entityMap.put(entity.getIdentifier(), entity);
    }

    public static Entity getEntity(String identifier) {
        return entityMap.get(identifier);
    }

    public static List<Entity> getEntities() {
        return new ArrayList<>(entityMap.values());
    }
}
