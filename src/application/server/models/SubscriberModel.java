package application.server.models;

import application.server.domain.entities.types.SimpleAbonneEntity;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SubscriberModel extends Model<SimpleAbonneEntity> {
    public SubscriberModel() {
        super();
    }

    @Override
    public void save(SimpleAbonneEntity entity) throws SQLException {
        int id = entity.getId();
        boolean banned = entity.isBanned();

        PreparedStatement preparedStatement = super.prepareStatement("UPDATE" + getFullTableName() + "SET isBanned = ? WHERE id = ?");
        preparedStatement.setBoolean(1, banned);
        preparedStatement.setInt(2, id);

        preparedStatement.executeUpdate();
    }

    @Override
    public String getTableName() {
        return "Subscriber";
    }

    @Override
    public SimpleAbonneEntity getEntityInstance() {
        return new SimpleAbonneEntity();
    }
}
