package ru.ylab.repository;

import ru.ylab.entity.Meter;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface MeterRepository {
    Set<Meter> getByUserId(Long userId);

    void save(Meter meter);

    void save(Collection<Meter> meters);

    Optional<Meter> findById(Long meterId);
}
