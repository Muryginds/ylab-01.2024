package ru.ylab.repository.impl;

import lombok.extern.slf4j.Slf4j;
import ru.ylab.entity.MeterReading;
import ru.ylab.exception.MonitoringServiceSQLExceptionException;
import ru.ylab.model.MeterReadingModel;
import ru.ylab.repository.MeterReadingsRepository;
import ru.ylab.utils.DbConnectionUtils;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class JdbcMeterReadingsRepository implements MeterReadingsRepository {

    @Override
    public Set<MeterReadingModel> getAllBySubmissionId(Long submissionId) {
        var selectQuery = "SELECT * FROM private.meter_readings WHERE submission_id = ?";
        var meterReadingModels = new HashSet<MeterReadingModel>();

        try (Connection connection = DbConnectionUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setLong(1, submissionId);

            try (var resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    MeterReadingModel meterReadingModel = mapResultSetToMeterReadingModel(resultSet);
                    meterReadingModels.add(meterReadingModel);
                }
            }

        } catch (SQLException e) {
            throw new MonitoringServiceSQLExceptionException(e);
        }

        return meterReadingModels;
    }

    @Override
    public void save(MeterReading meterReading) {
        var insertQuery = "INSERT INTO private.meter_readings (id, submission_id, meter_id, value) " +
                "VALUES (nextval('private.meter_readings_id_seq'), ?, ?, ?)";

        try (Connection connection = DbConnectionUtils.getConnection();
             var preparedStatement = connection.prepareStatement(insertQuery)) {

            preparedStatement.setLong(1, meterReading.getSubmission().getId());
            preparedStatement.setLong(2, meterReading.getMeter().getId());
            preparedStatement.setLong(3, meterReading.getValue());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new MonitoringServiceSQLExceptionException(e);
        }
    }

    @Override
    public void saveAll(Collection<MeterReading> meterReadings) {
        String insertQuery = "INSERT INTO private.meter_readings (id, submission_id, meter_id, value) " +
                "VALUES (nextval('private.meter_readings_id_seq'), ?, ?, ?)";

        try (var connection = DbConnectionUtils.getConnection();
             var preparedStatement = connection.prepareStatement(insertQuery)) {

            for (var meterReading : meterReadings) {
                preparedStatement.setLong(1, meterReading.getSubmission().getId());
                preparedStatement.setLong(2, meterReading.getMeter().getId());
                preparedStatement.setLong(3, meterReading.getValue());
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();

        } catch (SQLException e) {
            throw new MonitoringServiceSQLExceptionException(e);
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

