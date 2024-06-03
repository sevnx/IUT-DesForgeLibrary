package application.server.models;

import application.server.domain.Dvd;

public class DvdModel extends Model<Dvd> {

    /**
     * As the application doesn't handle modifying entities, this method is empty.
     *
     * @param entity The entity to save
     */
    @Override
    public void save(Dvd entity) {
    }

    @Override
    public String getTableName() {
        return "Dvd";
    }

    @Override
    public Dvd getEntityInstance() {
        return new Dvd();
    }
}
