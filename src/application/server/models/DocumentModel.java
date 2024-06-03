package application.server.models;

import application.server.domain.SimpleDocument;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DocumentModel extends Model<SimpleDocument> {


    @Override
    public void save(SimpleDocument entity) throws SQLException {
        int id = entity.getId();
        int newState = entity.getState().getStateId();

        PreparedStatement preparedStatement = super.prepareStatement("UPDATE" + getFullTableName() + "SET state = ? WHERE id = ?");
        preparedStatement.setInt(1, newState);
        preparedStatement.setInt(2, id);

        preparedStatement.executeUpdate();
    }

    @Override
    public String getTableName() {
        return "Document";
    }

    @Override
    public SimpleDocument getEntityInstance() {
        return new SimpleDocument();
    }
}
