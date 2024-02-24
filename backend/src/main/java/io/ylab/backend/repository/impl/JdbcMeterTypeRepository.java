package io.ylab.backend.repository.impl;

import io.ylab.commons.entity.MeterType;
import io.ylab.backend.model.MeterTypeModel;
import io.ylab.backend.repository.MeterTypeRepository;
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
public class JdbcMeterTypeRepository implements MeterTypeRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void save(MeterType meterType) {
        var sql = "INSERT INTO private.meter_types (type_name) VALUES (?)";
        var keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                con -> {
                    var preparedStatement = con.prepareStatement(sql, new String[]{"id"});
                    preparedStatement.setString(1, meterType.getTypeName());
                    return preparedStatement;
                },
                keyHolder
        );
        var optionalNumber = Optional.ofNullable(keyHolder.getKey());
        optionalNumber.ifPresent(num -> meterType.setId(num.longValue()));
    }

    @Override
    public void save(Collection<MeterType> meterTypes) {
        var sql = "INSERT INTO private.meter_types (type_name) VALUES (?)";
        jdbcTemplate.batchUpdate(sql, meterTypes, meterTypes.size(),
                (ps, meterType) -> ps.setString(1, meterType.getTypeName()));
    }

    @Override
    public Optional<MeterTypeModel> findById(Long meterTypeId) {
        var sql = "SELECT id, type_name FROM private.meter_types WHERE id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToMeterTypeModel(rs), meterTypeId)
                .stream()
                .findFirst();
    }

    @Override
    public Optional<MeterTypeModel> findByName(String meterTypeName) {
        var sql = "SELECT id, type_name FROM private.meter_types WHERE type_name = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToMeterTypeModel(rs), meterTypeName)
                .stream()
                .findFirst();
    }

    @Override
    public boolean checkExistsByName(String typeName) {
        var sql = "SELECT COUNT(id) FROM private.meter_types WHERE type_name = ?";
        var result = jdbcTemplate.queryForObject(sql, Integer.class, typeName);
        return Optional.ofNullable(result).orElse(0) > 0;
    }

    @Override
    public Collection<MeterTypeModel> getAll() {
        var sql = "SELECT id, type_name FROM private.meter_types";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToMeterTypeModel(rs));
    }

    private MeterTypeModel mapRowToMeterTypeModel(ResultSet rs) throws SQLException {
        return MeterTypeModel.builder()
                .id(rs.getLong("id"))
                .typeName(rs.getString("type_name"))
                .build();
    }
}
