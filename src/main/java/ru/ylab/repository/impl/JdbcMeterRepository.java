package ru.ylab.repository.impl;

import ru.ylab.entity.Meter;
import ru.ylab.exception.MonitoringServiceSQLExceptionException;
import ru.ylab.model.MeterModel;
import ru.ylab.repository.MeterRepository;
import ru.ylab.utils.DbConnectionUtils;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class JdbcMeterRepository implements MeterRepository {

    @Override
    public Set<MeterModel> getByUserId(Long userId) {
        var selectQuery = "SELECT * FROM private.meters WHERE user_id = ?";
        var meterModels = new HashSet<MeterModel>();

        try (var connection = DbConnectionUtils.getConnection();
             var preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setLong(1, userId);

            try (var resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    var meterModel = mapResultSetToMeterModel(resultSet);
                    meterModels.add(meterModel);
                }
            }

        } catch (SQLException e) {
            throw new MonitoringServiceSQLExceptionException(e);
        }

        return meterModels;
    }

    @Override
    public void save(Meter meter) {
        var insertQuery =
                "INSERT INTO private.meters (id, factory_number, user_id, meter_type_id) " +
                        "VALUES (nextval('private.meter_types_id_seq'), ?, ?, ?)";

        try (var connection = DbConnectionUtils.getConnection();
             var preparedStatement = connection.prepareStatement(insertQuery)) {

            preparedStatement.setString(1, meter.getFactoryNumber());
            preparedStatement.setLong(2, meter.getUser().getId());
            preparedStatement.setLong(3, meter.getMeterType().getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new MonitoringServiceSQLExceptionException(e);
        }
    }

    @Override
    public void save(Collection<Meter> meters) {
        var insertQuery =
                "INSERT INTO private.meters (id, factory_number, user_id, meter_type_id) " +
                        "VALUES (nextval('private.meter_types_id_seq'), ?, ?, ?)";

        try (var connection = DbConnectionUtils.getConnection();
             var preparedStatement = connection.prepareStatement(insertQuery)) {

            for (var meter : meters) {
                preparedStatement.setString(1, meter.getFactoryNumber());
                preparedStatement.setLong(2, meter.getUser().getId());
                preparedStatement.setLong(3, meter.getMeterType().getId());
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();

        } catch (SQLException e) {
            throw new MonitoringServiceSQLExceptionException(e);
        }
    }

    @Override
    public Optional<MeterModel> findById(Long meterId) {
        var selectQuery = "SELECT * FROM private.meters WHERE id = ?";

        try (var connection = DbConnectionUtils.getConnection();
             var preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setLong(1, meterId);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                var meterModel = mapResultSetToMeterModel(resultSet);
                return Optional.of(meterModel);
            }

        } catch (SQLException e) {
            throw new MonitoringServiceSQLExceptionException(e);
        }

        return Optional.empty();
    }

    private MeterModel mapResultSetToMeterModel(ResultSet resultSet) throws SQLException {
        return MeterModel.builder()
                .id(resultSet.getLong("id"))
                .factoryNumber(resultSet.getString("factory_number"))
                .userId(resultSet.getLong("user_id"))
                .meterTypeId(resultSet.getLong("meter_type_id"))
                .build();
    }
}
