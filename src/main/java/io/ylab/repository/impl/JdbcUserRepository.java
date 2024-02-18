package io.ylab.repository.impl;

import io.ylab.entity.User;
import io.ylab.enumerated.UserRole;
import io.ylab.model.UserModel;
import io.ylab.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean checkUserExistsByName(String username) {
        var sql = "SELECT COUNT(id) FROM private.users WHERE name = ?";
        var result = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return Optional.ofNullable(result).orElse(0) > 0;
    }

    @Override
    public boolean checkUserExistsById(Long userId) {
        var sql = "SELECT COUNT(id) FROM private.users WHERE id = ?";
        var result = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return Optional.ofNullable(result).orElse(0) > 0;
    }

    @Override
    public void save(User user) {
        var sql = "INSERT INTO private.users (name, password, role) VALUES (?, ?, ?)";
        var keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                con -> {
                    var preparedStatement = con.prepareStatement(sql, new String[]{"id"});
                    preparedStatement.setString(1, user.getName());
                    preparedStatement.setString(2, user.getPassword());
                    preparedStatement.setString(3, user.getRole().name());
                    return preparedStatement;
                },
                keyHolder
        );
        var optionalNumber = Optional.ofNullable(keyHolder.getKey());
        optionalNumber.ifPresent(num -> user.setId(num.longValue()));
    }

    @Override
    public Optional<UserModel> findUserByName(String name) {
        var sql = "SELECT id, name, password, role FROM private.users WHERE name = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapResultSetToUserModel(rs), name)
                .stream()
                .findFirst();
    }

    @Override
    public Optional<UserModel> findUserById(Long userId) {
        var sql = "SELECT id, name, password, role FROM private.users WHERE id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapResultSetToUserModel(rs), userId)
                .stream()
                .findFirst();
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
