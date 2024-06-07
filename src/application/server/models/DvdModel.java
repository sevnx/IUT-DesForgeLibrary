package application.server.models;

import application.server.domain.entities.types.DvdEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DvdModel extends Model<DvdEntity> {
    private static final Logger LOGGER = LogManager.getLogger("Dvd - DB Model");

    /**
     * As the application doesn't handle modifying entities, this method is empty.
     *
     * @param entity The entity to save
     */
    @Override
    public void save(DvdEntity entity) {
        LOGGER.warn("Attempted to save DVD, but this operation is not supported");
    }

    @Override
    public String getTableName() {
        return "Dvd";
    }

    @Override
    public DvdEntity getEntityInstance() {
        return new DvdEntity();
    }
}
