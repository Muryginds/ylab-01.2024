package ru.ylab.repository.impl;

import lombok.RequiredArgsConstructor;
import ru.ylab.entity.MeterReading;
import ru.ylab.model.MeterReadingModel;
import ru.ylab.repository.MeterReadingsRepository;
import ru.ylab.utils.DbConnectionFactory;
import ru.ylab.utils.ExceptionHandler;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class JdbcMeterReadingsRepository implements MeterReadingsRepository {
    private final DbConnectionFactory dbConnectionFactory;

    @Override
    public Set<MeterReadingModel> getAllBySubmissionId(Long submissionId) {
        var selectQuery = "SELECT * FROM private.meter_readings WHERE submission_id = ?";
        var meterReadingModels = new HashSet<MeterReadingModel>();

        try (Connection connection = dbConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setLong(1, submissionId);

            try (var resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    MeterReadingModel meterReadingModel = mapResultSetToMeterReadingModel(resultSet);
                    meterReadingModels.add(meterReadingModel);
                }
            }

        } catch (SQLException e) {
            ExceptionHandler.handleSQLException(e);
        }

        return meterReadingModels;
    }

    @Override
    public void save(MeterReading meterReading) {
        var insertQuery = "INSERT INTO private.meter_readings (submission_id, meter_id, value) VALUES (?, ?, ?)";

        try (Connection connection = dbConnectionFactory.getConnection();
             var preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setLong(1, meterReading.getSubmission().getId());
            preparedStatement.setLong(2, meterReading.getMeter().getId());
            preparedStatement.setLong(3, meterReading.getValue());
            preparedStatement.executeUpdate();

            try (var resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    var generatedId = resultSet.getLong(1);
                    meterReading.setId(generatedId);
                }
            }

        } catch (SQLException e) {
            ExceptionHandler.handleSQLException(e);
        }
    }

    @Override
    public void saveAll(Collection<MeterReading> meterReadings) {
        String insertQuery = "INSERT INTO private.meter_readings (submission_id, meter_id, value) VALUES (?, ?, ?)";

        try (var connection = dbConnectionFactory.getConnection();
             var preparedStatement = connection.prepareStatement(insertQuery)) {

            for (var meterReading : meterReadings) {
                preparedStatement.setLong(1, meterReading.getSubmission().getId());
                preparedStatement.setLong(2, meterReading.getMeter().getId());
                preparedStatement.setLong(3, meterReading.getValue());
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();

        } catch (SQLException e) {
            ExceptionHandler.handleSQLException(e);
        }
    }

    private MeterReadingModel mapResultSetToMeterReadingModel(ResultSet resultSet) throws SQLException {
        return MeterReadingModel.builder()
                .id(resultSet.getLong("id"))
                .submissionId(resultSet.getLong("submission_id"))
                .meterId(resultSet.getLong("meter_id"))
                .value(resultSet.getLong("value"))
                .build();
    }
}

