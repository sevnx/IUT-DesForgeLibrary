package application.server.models;


import application.server.domain.entities.types.DocumentLogEntity;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class DocumentLogModel extends Model<DocumentLogEntity> {
    @Override
    public void save(DocumentLogEntity entity) throws SQLException {
        if (exists(entity)) {
            runUpdate(entity);
        } else {
            runInsert(entity);
        }
    }

    private void runInsert(DocumentLogEntity entity) throws SQLException {
        PreparedStatement preparedStatement = super.prepareStatement("INSERT INTO " + getFullTableName() + " (idDocument, idSubscriber, idNewState, date) VALUES (?, ?, ?, ?)");

        preparedStatement.setInt(1, entity.getDocument().numero());
        if (entity.getSubscriber().isPresent()) {
            preparedStatement.setInt(2, entity.getSubscriber().get().getId());
        } else {
            preparedStatement.setNull(2, java.sql.Types.INTEGER);
        }
        preparedStatement.setInt(3, entity.getNewState().getStateId());
        preparedStatement.setTimestamp(4, Timestamp.valueOf(entity.getTime()));

        preparedStatement.executeUpdate();
    }

    private void runUpdate(DocumentLogEntity entity) throws SQLException {
        PreparedStatement preparedStatement = super.prepareStatement("UPDATE " + getFullTableName() + " SET idSubscriber = ?, idNewState = ?, date = ? WHERE id = ?");

        if (entity.getSubscriber().isPresent()) {
            preparedStatement.setInt(1, entity.getSubscriber().get().getId());
        } else {
            preparedStatement.setNull(1, java.sql.Types.INTEGER);
        }

        preparedStatement.setInt(2, entity.getNewState().getStateId());
        preparedStatement.setTimestamp(3, Timestamp.valueOf(entity.getTime()));
        preparedStatement.setInt(4, entity.getId());

        preparedStatement.executeUpdate();
    }

    private boolean exists(DocumentLogEntity entity) throws SQLException {
        PreparedStatement preparedStatement = super.prepareStatement("SELECT * FROM " + getFullTableName() + " WHERE idDocument = ?)");
        preparedStatement.setInt(1, entity.getId());
        return preparedStatement.executeQuery().next();
    }

    @Override
    public String getTableName() {
        return "DocumentLog";
    }

    @Override
    public DocumentLogEntity getEntityInstance() {
        return new DocumentLogEntity();
    }
}
