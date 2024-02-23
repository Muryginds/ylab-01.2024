package io.ylab.backend.repository.impl;

import io.ylab.backend.entity.Meter;
import io.ylab.backend.model.MeterModel;
import io.ylab.backend.repository.MeterRepository;
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
public class JdbcMeterRepository implements MeterRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<MeterModel> getByUserId(Long userId) {
        var sql = "SELECT id, user_id, factory_number, meter_type_id FROM private.meters WHERE user_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToMeterModel(rs), userId);
    }

    @Override
    public void save(Meter meter) {
        var sql = "INSERT INTO private.meters (factory_number, user_id, meter_type_id) VALUES (?, ?, ?)";
        var keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                con -> {
                    var preparedStatement = con.prepareStatement(sql, new String[]{"id"});
                    preparedStatement.setString(1, meter.getFactoryNumber());
                    preparedStatement.setLong(2, meter.getUser().getId());
                    preparedStatement.setLong(3, meter.getMeterType().getId());
                    return preparedStatement;
                },
                keyHolder
        );
        var optionalNumber = Optional.ofNullable(keyHolder.getKey());
        optionalNumber.ifPresent(num -> meter.setId(num.longValue()));
    }

    @Override
    public void save(Collection<Meter> meters) {
        var sql = "INSERT INTO private.meters (factory_number, user_id, meter_type_id) VALUES (?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, meters, meters.size(),
                (ps, meter) -> {
                    ps.setString(1, meter.getFactoryNumber());
                    ps.setLong(2, meter.getUser().getId());
                    ps.setLong(3, meter.getMeterType().getId());
                });
    }

    @Override
    public Optional<MeterModel> findById(Long meterId) {
        var sql = "SELECT id, user_id, factory_number, meter_type_id FROM private.meters WHERE id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToMeterModel(rs), meterId)
                .stream()
                .findFirst();
    }

    private MeterModel mapRowToMeterModel(ResultSet rs) throws SQLException {
        return MeterModel.builder()
                .id(rs.getLong("id"))
                .factoryNumber(rs.getString("factory_number"))
                .userId(rs.getLong("user_id"))
                .meterTypeId(rs.getLong("meter_type_id"))
                .build();
    }
}
