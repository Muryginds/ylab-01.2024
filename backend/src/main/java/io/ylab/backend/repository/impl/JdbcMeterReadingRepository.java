package io.ylab.backend.repository.impl;

import io.ylab.backend.entity.MeterReading;
import io.ylab.backend.model.MeterReadingModel;
import io.ylab.backend.repository.MeterReadingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcMeterReadingRepository implements MeterReadingRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<MeterReadingModel> getAllBySubmissionId(Long submissionId) {
        var sql = "SELECT id, meter_id, value, submission_id FROM private.meter_readings WHERE submission_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToMeterReadingModel(rs), submissionId);
    }

    @Override
    public void save(MeterReading meterReading) {
        var sql = "INSERT INTO private.meter_readings (submission_id, meter_id, value) VALUES (?, ?, ?)";
        var keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                con -> {
                    var preparedStatement = con.prepareStatement(sql, new String[]{"id"});
                    preparedStatement.setLong(1, meterReading.getSubmission().getId());
                    preparedStatement.setLong(2, meterReading.getMeter().getId());
                    preparedStatement.setLong(3, meterReading.getValue());
                    return preparedStatement;
                },
                keyHolder
        );
        var optionalNumber = Optional.ofNullable(keyHolder.getKey());
        optionalNumber.ifPresent(num -> meterReading.setId(num.longValue()));
    }

    @Override
    public void saveAll(Collection<MeterReading> meterReadings) {
        var sql = "INSERT INTO private.meter_readings (submission_id, meter_id, value) VALUES (?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, meterReadings, meterReadings.size(),
                (ps, meterReading) -> {
                    ps.setLong(1, meterReading.getSubmission().getId());
                    ps.setLong(2, meterReading.getMeter().getId());
                    ps.setLong(3, meterReading.getValue());
                });
    }

    private MeterReadingModel mapRowToMeterReadingModel(ResultSet rs) throws SQLException {
        return MeterReadingModel.builder()
                .id(rs.getLong("id"))
                .submissionId(rs.getLong("submission_id"))
                .meterId(rs.getLong("meter_id"))
                .value(rs.getLong("value"))
                .build();
    }
}
