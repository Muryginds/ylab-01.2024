package ru.ylab.repository;

import ru.ylab.entity.MeterType;

import java.util.Collection;
import java.util.Optional;

public interface MeterTypeRepository {
    void save(MeterType meterType);

    void save(Collection<MeterType> meterTypes);

    Optional<MeterType> findById(Long meterTypeId);

    boolean checkExistsByName(String typeName);

    Collection<MeterType> getAll();
}
