package io.ylab.backend.repository.impl;

import io.ylab.commons.entity.Submission;
import io.ylab.backend.model.SubmissionModel;
import io.ylab.backend.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcSubmissionRepository implements SubmissionRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<SubmissionModel> getByUserId(Long userId) {
        var sql = "SELECT id, user_id, date FROM private.submissions WHERE user_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToSubmissionModel(rs), userId);
    }

    @Override
    public Optional<SubmissionModel> getById(Long submissionId) {
        var sql = "SELECT id, user_id, date FROM private.submissions WHERE id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToSubmissionModel(rs), submissionId)
                .stream()
                .findFirst();
    }

    @Override
    public void save(Submission submission) {
        var sql = "INSERT INTO private.submissions (user_id, date) VALUES (?, ?)";
        var keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                con -> {
                    var preparedStatement = con.prepareStatement(sql, new String[]{"id"});
                    preparedStatement.setLong(1, submission.getUser().getId());
                    preparedStatement.setDate(2, Date.valueOf(submission.getDate()));
                    return preparedStatement;
                },
                keyHolder
        );
        var optionalNumber = Optional.ofNullable(keyHolder.getKey());
        optionalNumber.ifPresent(num -> submission.setId(num.longValue()));
    }

    @Override
    public boolean checkExistsByUserIdAndDate(Long userId, LocalDate date) {
        var sql = "SELECT COUNT(id) FROM private.submissions WHERE user_id = ? AND date = ?";
        var result = jdbcTemplate.queryForObject(sql, Integer.class, userId, date);
        return Optional.ofNullable(result).orElse(0) > 0;
    }

    @Override
    public Optional<SubmissionModel> findSubmissionByUserIdAndDate(Long userId, LocalDate date) {
        var sql = "SELECT id, user_id, date FROM private.submissions WHERE user_id = ? " +
                "AND EXTRACT(MONTH FROM date) = ? AND EXTRACT(YEAR FROM date) = ?";
        return jdbcTemplate.query(
                sql, (rs, rowNum) -> mapRowToSubmissionModel(rs), userId, date.getMonthValue(), date.getYear()
                )
                .stream()
                .findFirst();
    }

    @Override
    public Optional<SubmissionModel> findLastSubmissionByUserId(Long userId) {
        var sql = "SELECT id, user_id, date FROM private.submissions " +
                "WHERE user_id = ? ORDER BY date DESC LIMIT 1";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToSubmissionModel(rs), userId)
                .stream()
                .findFirst();
    }

    private SubmissionModel mapRowToSubmissionModel(ResultSet resultSet) throws SQLException {
        return SubmissionModel.builder()
                .id(resultSet.getLong("id"))
                .userId(resultSet.getLong("user_id"))
                .date(resultSet.getDate("date").toLocalDate())
                .build();
    }
}
