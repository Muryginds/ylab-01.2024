package ru.ylab.repository.impl;

import ru.ylab.entity.MeterType;
import ru.ylab.repository.MeterTypeRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryMeterTypeRepository implements MeterTypeRepository {
    private static final Map<Long, MeterType> METER_TYPES = init();

    private static Map<Long, MeterType> init() {
        var types = new MeterType[]{
                MeterType.builder().typeName("HEATING").build(),
                MeterType.builder().typeName("COLD_WATER").build(),
                MeterType.builder().typeName("HOT_WATER").build(),
        };
        var map = new HashMap<Long, MeterType>();
        for (var type : types) {
            map.put(type.getId(), type);
        }
        return map;
    }

    @Override
    public void save(MeterType meterType) {
        METER_TYPES.put(meterType.getId(), meterType);
    }

    @Override
    public void save(Collection<MeterType> meterTypes) {
        meterTypes.forEach(this::save);
    }

    @Override
    public Optional<MeterType> findById(Long meterTypeId) {
        return Optional.ofNullable(METER_TYPES.get(meterTypeId));
    }

    @Override
    public boolean checkExistsByName(String typeName) {
        return METER_TYPES.values().stream()
                .anyMatch(mt -> mt.getTypeName()
                        .equals(typeName)
                );
    }

    @Override
    public Collection<MeterType> getAll() {
        return METER_TYPES.values();
    }
}
