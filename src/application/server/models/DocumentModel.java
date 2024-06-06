package application.server.models;

import application.server.domain.entities.types.SimpleDocumentEntity;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DocumentModel extends Model<SimpleDocumentEntity> {


    @Override
    public void save(SimpleDocumentEntity entity) throws SQLException {
        int id = entity.getId();
        int newState = entity.getState().getStateId();

        PreparedStatement preparedStatement = super.prepareStatement("UPDATE " + getFullTableName() + " SET idState = ? WHERE id = ?");
        preparedStatement.setInt(1, newState);
        preparedStatement.setInt(2, id);

        preparedStatement.executeUpdate();
    }

    @Override
    public String getTableName() {
        return "Document";
    }

    @Override
    public SimpleDocumentEntity getEntityInstance() {
        return new SimpleDocumentEntity();
    }
}
