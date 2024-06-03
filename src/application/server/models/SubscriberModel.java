package application.server.models;

import application.server.domain.SimpleAbonne;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SubscriberModel extends Model<SimpleAbonne> {
    public SubscriberModel() {
        super();
    }

    @Override
    public void save(SimpleAbonne entity) throws SQLException {
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
    public SimpleAbonne getEntityInstance() {
        return new SimpleAbonne();
    }
}
