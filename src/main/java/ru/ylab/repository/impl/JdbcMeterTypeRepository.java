package ru.ylab.repository.impl;

import lombok.extern.slf4j.Slf4j;
import ru.ylab.entity.MeterType;
import ru.ylab.exception.MonitoringServiceSQLExceptionException;
import ru.ylab.model.MeterTypeModel;
import ru.ylab.repository.MeterTypeRepository;
import ru.ylab.utils.DbConnectionUtils;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

@Slf4j
public class JdbcMeterTypeRepository implements MeterTypeRepository {

    @Override
    public void save(MeterType meterType) {
        var insertQuery = "INSERT INTO private.meter_types (id, type_name) " +
                "VALUES (nextval('private.meter_types_id_seq'), ?)";

        try (var connection = DbConnectionUtils.getConnection();
             var preparedStatement = connection.prepareStatement(insertQuery)) {

            preparedStatement.setString(1, meterType.getTypeName());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new MonitoringServiceSQLExceptionException(e);
        }
    }

    @Override
    public void save(Collection<MeterType> meterTypes) {
        var insertQuery = "INSERT INTO private.meter_types (id, type_name) " +
                "VALUES (nextval('private.meter_types_id_seq'), ?)";

        try (var connection = DbConnectionUtils.getConnection();
             var preparedStatement = connection.prepareStatement(insertQuery)) {

            for (var meterType : meterTypes) {
                preparedStatement.setString(1, meterType.getTypeName());
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();

        } catch (SQLException e) {
            throw new MonitoringServiceSQLExceptionException(e);
        }
    }

    @Override
    public Optional<MeterTypeModel> findById(Long meterTypeId) {
        String selectQuery = "SELECT * FROM private.meter_types WHERE id = ?";

        try (Connection connection = DbConnectionUtils.getConnection();
             var preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setLong(1, meterTypeId);

            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    var meterTypeModel = mapResultSetToMeterTypeModel(resultSet);
                    return Optional.of(meterTypeModel);
                }
            }

        } catch (SQLException e) {
            throw new MonitoringServiceSQLExceptionException(e);
        }

        return Optional.empty();
    }

    @Override
    public boolean checkExistsByName(String typeName) {
        String selectQuery = "SELECT COUNT(*) FROM private.meter_types WHERE type_name = ?";

        try (var connection = DbConnectionUtils.getConnection();
             var preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setString(1, typeName);

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
    public Collection<MeterTypeModel> getAll() {
        var selectQuery = "SELECT * FROM private.meter_types";
        var meterTypes = new HashSet<MeterTypeModel>();

        try (var connection = DbConnectionUtils.getConnection();
             var preparedStatement = connection.prepareStatement(selectQuery)) {

            try (var resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    var meterType = mapResultSetToMeterTypeModel(resultSet);
                    meterTypes.add(meterType);
                }
            }

        } catch (SQLException e) {
            throw new MonitoringServiceSQLExceptionException(e);
        }

        return meterTypes;
    }

    private MeterTypeModel mapResultSetToMeterTypeModel(ResultSet resultSet) throws SQLException {
        return MeterTypeModel.builder()
                .id(resultSet.getLong("id"))
                .typeName(resultSet.getString("type_name"))
                .build();
    }
}