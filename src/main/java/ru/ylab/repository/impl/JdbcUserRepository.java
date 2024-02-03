package ru.ylab.repository.impl;

import lombok.extern.slf4j.Slf4j;
import ru.ylab.entity.User;
import ru.ylab.enumerated.UserRole;
import ru.ylab.exception.MonitoringServiceSQLExceptionException;
import ru.ylab.model.UserModel;
import ru.ylab.repository.UserRepository;
import ru.ylab.utils.DbConnectionUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

@Slf4j
public class JdbcUserRepository implements UserRepository {

    @Override
    public boolean checkUserExistsByName(String username) {
        var selectQuery = "SELECT COUNT(*) FROM private.users WHERE name = ?";

        try (var connection = DbConnectionUtils.getConnection();
             var preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setString(1, username);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getLong(1) > 0;
            }

        } catch (SQLException e) {
            throw new MonitoringServiceSQLExceptionException(e);
        }

        return false;
    }

    @Override
    public boolean checkUserExistsById(Long userId) {
        var selectQuery = "SELECT COUNT(*) FROM private.users WHERE id = ?";

        try (var connection = DbConnectionUtils.getConnection();
             var preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setLong(1, userId);

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
    public void save(User user) {
        var insertQuery = "INSERT INTO private.users (id, name, password, role) " +
                "VALUES (nextval('private.users_id_seq'), ?, ?, ?) RETURNING id";

        try (var connection = DbConnectionUtils.getConnection();
             var preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getRole().name());
            preparedStatement.executeUpdate();

            try (var resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    var generatedId = resultSet.getLong(1);
                    user.setId(generatedId);
                }
            }
        } catch (SQLException e) {
            throw new MonitoringServiceSQLExceptionException(e);
        }
    }

    @Override
    public Optional<UserModel> findUserByName(String name) {
        var selectQuery = "SELECT * FROM private.users WHERE name = ?";

        try (var connection = DbConnectionUtils.getConnection();
             var preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setString(1, name);

            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    var userModel = mapResultSetToUserModel(resultSet);
                    return Optional.of(userModel);
                }
            }

        } catch (SQLException e) {
            throw new MonitoringServiceSQLExceptionException(e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<UserModel> findUserById(Long userId) {
        var selectQuery = "SELECT * FROM private.users WHERE id = ?";

        try (var connection = DbConnectionUtils.getConnection();
             var preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setLong(1, userId);

            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    var userModel = mapResultSetToUserModel(resultSet);
                    return Optional.of(userModel);
                }
            }

        } catch (SQLException e) {
            throw new MonitoringServiceSQLExceptionException(e);
        }

        return Optional.empty();
    }

    private UserModel mapResultSetToUserModel(ResultSet resultSet) throws SQLException {
        return UserModel.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .password(resultSet.getString("password"))
                .role(UserRole.valueOf(resultSet.getString("role")))
                .build();
    }
}
