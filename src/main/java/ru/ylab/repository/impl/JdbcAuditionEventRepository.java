package ru.ylab.repository.impl;

import lombok.RequiredArgsConstructor;
import ru.ylab.entity.AuditionEvent;
import ru.ylab.enumerated.AuditionEventType;
import ru.ylab.model.AuditionEventModel;
import ru.ylab.repository.AuditionEventRepository;
import ru.ylab.utils.DbConnectionFactory;
import ru.ylab.utils.ExceptionHandler;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;

@RequiredArgsConstructor
public class JdbcAuditionEventRepository implements AuditionEventRepository {
    private final DbConnectionFactory dbConnectionFactory;

    @Override
    public Collection<AuditionEventModel> getEventsByUserId(Long userId) {
        var selectQuery = "SELECT * FROM private.audition_events WHERE user_id = ?";
        var auditionEventModels = new HashSet<AuditionEventModel>();

        try (Connection connection = dbConnectionFactory.getConnection();
             var preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setLong(1, userId);
            try (var resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    AuditionEventModel auditionEventModel = mapResultSetToAuditionEventModel(resultSet);
                    auditionEventModels.add(auditionEventModel);
                }
            }

        } catch (SQLException e) {
            ExceptionHandler.handleSQLException(e);
        }

        return auditionEventModels;
    }

    @Override
    public void save(AuditionEvent auditionEvent) {
        String insertQuery = "INSERT INTO private.audition_events (user_id, event_type, message, date) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection connection = dbConnectionFactory.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setLong(1, auditionEvent.getUser().getId());
            preparedStatement.setString(2, auditionEvent.getEventType().name());
            preparedStatement.setString(3, auditionEvent.getMessage());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(auditionEvent.getDate()));
            preparedStatement.executeUpdate();

            try (var resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    var generatedId = resultSet.getLong(1);
                    auditionEvent.setId(generatedId);
                }
            }

        } catch (SQLException e) {
            ExceptionHandler.handleSQLException(e);
        }
    }

    private AuditionEventModel mapResultSetToAuditionEventModel(ResultSet resultSet) throws SQLException {
        return AuditionEventModel.builder()
                .id(resultSet.getLong("id"))
                .userId(resultSet.getLong("user_id"))
                .eventType(AuditionEventType.valueOf(resultSet.getString("event_type")))
                .message(resultSet.getString("message"))
                .date(resultSet.getTimestamp("date").toLocalDateTime())
                .build();
    }
}
