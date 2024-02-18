package io.ylab.repository.impl;

import io.ylab.entity.AuditionEvent;
import io.ylab.enumerated.AuditionEventType;
import io.ylab.model.AuditionEventModel;
import io.ylab.repository.AuditionEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcAuditionEventRepository implements AuditionEventRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<AuditionEventModel> getEventsByUserId(Long userId) {
        var sql = "SELECT id, user_id, event_type, message, date FROM private.audition_events " +
                "WHERE user_id = ? ORDER BY date DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToAuditionEventModel(rs), userId);
    }

    @Override
    public void save(AuditionEvent auditionEvent) {
        var sql = "INSERT INTO private.audition_events (user_id, event_type, message, date) VALUES (?, ?, ?, ?)";
        var keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                con -> {
                    var preparedStatement =
                            con.prepareStatement(sql, new String[]{"id"});
                    preparedStatement.setLong(1, auditionEvent.getUser().getId());
                    preparedStatement.setString(2, auditionEvent.getEventType().name());
                    preparedStatement.setString(3, auditionEvent.getMessage());
                    preparedStatement.setTimestamp(4, Timestamp.valueOf(auditionEvent.getDate()));
                    return preparedStatement;
                }, keyHolder);
        var optionalNumber = Optional.ofNullable(keyHolder.getKey());
        optionalNumber.ifPresent(num -> auditionEvent.setId(num.longValue()));
    }

    private AuditionEventModel mapRowToAuditionEventModel(ResultSet rs) throws SQLException {
        return AuditionEventModel.builder()
                .id(rs.getLong("id"))
                .userId(rs.getLong("user_id"))
                .eventType(AuditionEventType.valueOf(rs.getString("event_type")))
                .message(rs.getString("message"))
                .date(rs.getTimestamp("date").toLocalDateTime())
                .build();
    }
}
