package io.ylab.audition.repository;

import io.ylab.commons.entity.AuditionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.Timestamp;
import java.util.Optional;

@RequiredArgsConstructor
public class EventRepository {
    private final JdbcTemplate jdbcTemplate;

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
}
