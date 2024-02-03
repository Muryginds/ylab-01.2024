package ru.ylab.repository.impl;

import lombok.extern.slf4j.Slf4j;
import ru.ylab.entity.Submission;
import ru.ylab.exception.MonitoringServiceSQLExceptionException;
import ru.ylab.model.SubmissionModel;
import ru.ylab.repository.SubmissionRepository;
import ru.ylab.utils.DbConnectionUtils;

import java.sql.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

@Slf4j
public class JdbcSubmissionRepository implements SubmissionRepository {

    @Override
    public Collection<SubmissionModel> getByUserId(Long userId) {
        var selectQuery = "SELECT * FROM private.submissions WHERE user_id = ?";
        var submissionModels = new HashSet<SubmissionModel>();

        try (var connection = DbConnectionUtils.getConnection();
             var preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setLong(1, userId);
            try (var resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    var submissionModel = mapResultSetToSubmissionModel(resultSet);
                    submissionModels.add(submissionModel);
                }
            }

        } catch (SQLException e) {
            throw new MonitoringServiceSQLExceptionException(e);
        }

        return submissionModels;
    }

    @Override
    public Optional<SubmissionModel> getById(Long submissionId) {
        var selectQuery = "SELECT * FROM private.submissions WHERE id = ?";

        try (var connection = DbConnectionUtils.getConnection();
             var preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setLong(1, submissionId);
            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToSubmissionModel(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new MonitoringServiceSQLExceptionException(e);
        }

        return Optional.empty();
    }

    @Override
    public void save(Submission submission) {
        var insertQuery = "INSERT INTO private.submissions (id, user_id, date) " +
                "VALUES (nextval('private.submissions_id_seq'), ?, ?) RETURNING id";

        try (var connection = DbConnectionUtils.getConnection();
             var preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setLong(1, submission.getUser().getId());
            preparedStatement.setDate(2, Date.valueOf(submission.getDate()));
            preparedStatement.executeUpdate();

            try (var resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    var generatedId = resultSet.getLong(1);
                    submission.setId(generatedId);
                }
            }
        } catch (SQLException e) {
            throw new MonitoringServiceSQLExceptionException(e);
        }
    }

    @Override
    public boolean checkExistsByUserIdAndDate(Long userId, LocalDate date) {
        var selectQuery = "SELECT COUNT(*) FROM private.submissions WHERE user_id = ? AND date = ?";

        try (var connection = DbConnectionUtils.getConnection();
             var preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setLong(1, userId);
            preparedStatement.setDate(2, Date.valueOf(date));

            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong(1) > 0;
                }
            }

        } catch (SQLException e) {
            throw new MonitoringServiceSQLExceptionException(e);
        }

        return false;
    }

    @Override
    public Optional<SubmissionModel> findSubmissionByUserIdAndDate(Long userId, LocalDate date) {
        var selectQuery = "SELECT * FROM private.submissions WHERE user_id = ? " +
                "AND EXTRACT(MONTH FROM date) = ? AND EXTRACT(YEAR FROM date) = ?";
        try (var connection = DbConnectionUtils.getConnection();
             var preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setLong(1, userId);
            preparedStatement.setInt(2, date.getMonthValue());
            preparedStatement.setInt(3, date.getYear());

            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    var submissionModel = mapResultSetToSubmissionModel(resultSet);
                    return Optional.of(submissionModel);
                }
            }

        } catch (SQLException e) {
            throw new MonitoringServiceSQLExceptionException(e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<SubmissionModel> findLastSubmissionByUserId(Long userId) {
        var selectQuery = "SELECT * FROM private.submissions WHERE user_id = ? ORDER BY date DESC LIMIT 1";

        try (var connection = DbConnectionUtils.getConnection();
             var preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setLong(1, userId);

            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    var submissionModel = mapResultSetToSubmissionModel(resultSet);
                    return Optional.of(submissionModel);
                }
            }

        } catch (SQLException e) {
            throw new MonitoringServiceSQLExceptionException(e);
        }

        return Optional.empty();
    }

    private SubmissionModel mapResultSetToSubmissionModel(ResultSet resultSet) throws SQLException {
        return SubmissionModel.builder()
                .id(resultSet.getLong("id"))
                .userId(resultSet.getLong("user_id"))
                .date(resultSet.getDate("date").toLocalDate())
                .build();
    }
}
