package application.server.models;


import application.server.domain.DocumentLog;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DocumentLogModel extends Model<DocumentLog> {
    @Override
    public void save(DocumentLog entity) throws SQLException {
        PreparedStatement preparedStatement = super.prepareStatement("INSERT INTO " + getFullTableName() + " (idDocument, idSubscriber, idNewState, date) VALUES (?, ?, ?, NOW())");

        preparedStatement.setInt(1, entity.getId());
        if (entity.getSubscriber().isPresent()) {
            preparedStatement.setInt(2, entity.getSubscriber().get().getId());
        } else {
            preparedStatement.setNull(2, java.sql.Types.INTEGER);
        }
        preparedStatement.setInt(3, entity.getNewState().getStateId());
    }

    @Override
    public String getTableName() {
        return "DocumentLog";
    }

    @Override
    public DocumentLog getEntityInstance() {
        return new DocumentLog();
    }
}
