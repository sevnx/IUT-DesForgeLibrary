package application.server.models;

import application.server.domain.entities.types.DvdEntity;

public class DvdModel extends Model<DvdEntity> {

    /**
     * As the application doesn't handle modifying entities, this method is empty.
     *
     * @param entity The entity to save
     */
    @Override
    public void save(DvdEntity entity) {
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
