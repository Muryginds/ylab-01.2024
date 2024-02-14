package io.ylab.repository.impl;

import lombok.RequiredArgsConstructor;
import io.ylab.entity.MeterType;
import io.ylab.model.MeterTypeModel;
import io.ylab.repository.MeterTypeRepository;
import io.ylab.utils.DbConnectionFactory;
import io.ylab.utils.ExceptionHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

@RequiredArgsConstructor
public class JdbcMeterTypeRepository implements MeterTypeRepository {
    private final DbConnectionFactory dbConnectionFactory;

    @Override
    public void save(MeterType meterType) {
        var insertQuery = "INSERT INTO private.meter_types (type_name) VALUES (?)";

        try (var connection = dbConnectionFactory.getConnection();
             var preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, meterType.getTypeName());
            preparedStatement.executeUpdate();

            try (var resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    var generatedId = resultSet.getLong(1);
                    meterType.setId(generatedId);
                }
            }

        } catch (SQLException e) {
            ExceptionHandler.handleSQLException(e);
        }
    }

    @Override
    public void save(Collection<MeterType> meterTypes) {
        var insertQuery = "INSERT INTO private.meter_types (type_name) VALUES (?)";

        try (var connection = dbConnectionFactory.getConnection();
             var preparedStatement = connection.prepareStatement(insertQuery)) {

            for (var meterType : meterTypes) {
                preparedStatement.setString(1, meterType.getTypeName());
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();

        } catch (SQLException e) {
            ExceptionHandler.handleSQLException(e);
        }
    }

    @Override
    public Optional<MeterTypeModel> findById(Long meterTypeId) {
        String selectQuery = "SELECT id, type_name FROM private.meter_types WHERE id = ?";

        try (Connection connection = dbConnectionFactory.getConnection();
             var preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setLong(1, meterTypeId);

            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    var meterTypeModel = mapResultSetToMeterTypeModel(resultSet);
                    return Optional.of(meterTypeModel);
                }
            }

        } catch (SQLException e) {
            ExceptionHandler.handleSQLException(e);
        }

        return Optional.empty();
    }

    @Override
    public boolean checkExistsByName(String typeName) {
        String selectQuery = "SELECT COUNT(id) FROM private.meter_types WHERE type_name = ?";

        try (var connection = dbConnectionFactory.getConnection();
             var preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setString(1, typeName);

            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong(1) > 0;
                }
            }

        } catch (SQLException e) {
            ExceptionHandler.handleSQLException(e);
        }

        return false;
    }

    @Override
    public Collection<MeterTypeModel> getAll() {
        var selectQuery = "SELECT id, type_name FROM private.meter_types";
        var meterTypes = new HashSet<MeterTypeModel>();

        try (var connection = dbConnectionFactory.getConnection();
             var preparedStatement = connection.prepareStatement(selectQuery)) {

            try (var resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    var meterType = mapResultSetToMeterTypeModel(resultSet);
                    meterTypes.add(meterType);
                }
            }

        } catch (SQLException e) {
            ExceptionHandler.handleSQLException(e);
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
