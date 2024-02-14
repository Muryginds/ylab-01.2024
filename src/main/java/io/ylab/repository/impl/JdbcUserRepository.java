package io.ylab.repository.impl;

import lombok.RequiredArgsConstructor;
import io.ylab.entity.User;
import io.ylab.enumerated.UserRole;
import io.ylab.model.UserModel;
import io.ylab.repository.UserRepository;
import io.ylab.utils.DbConnectionFactory;
import io.ylab.utils.ExceptionHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {
    private final DbConnectionFactory dbConnectionFactory;

    @Override
    public boolean checkUserExistsByName(String username) {
        var selectQuery = "SELECT COUNT(id) FROM private.users WHERE name = ?";

        try (var connection = dbConnectionFactory.getConnection();
             var preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setString(1, username);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getLong(1) > 0;
            }

        } catch (SQLException e) {
            ExceptionHandler.handleSQLException(e);
        }

        return false;
    }

    @Override
    public boolean checkUserExistsById(Long userId) {
        var selectQuery = "SELECT COUNT(id) FROM private.users WHERE id = ?";

        try (var connection = dbConnectionFactory.getConnection();
             var preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setLong(1, userId);

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
    public void save(User user) {
        var insertQuery = "INSERT INTO private.users (name, password, role) VALUES (?, ?, ?)";

        try (var connection = dbConnectionFactory.getConnection();
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
            ExceptionHandler.handleSQLException(e);
        }
    }

    @Override
    public Optional<UserModel> findUserByName(String name) {
        var selectQuery = "SELECT id, name, password, role FROM private.users WHERE name = ?";

        try (var connection = dbConnectionFactory.getConnection();
             var preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setString(1, name);

            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    var userModel = mapResultSetToUserModel(resultSet);
                    return Optional.of(userModel);
                }
            }

        } catch (SQLException e) {
            ExceptionHandler.handleSQLException(e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<UserModel> findUserById(Long userId) {
        var selectQuery = "SELECT id, name, password, role FROM private.users WHERE id = ?";

        try (var connection = dbConnectionFactory.getConnection();
             var preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setLong(1, userId);

            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    var userModel = mapResultSetToUserModel(resultSet);
                    return Optional.of(userModel);
                }
            }

        } catch (SQLException e) {
            ExceptionHandler.handleSQLException(e);
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
