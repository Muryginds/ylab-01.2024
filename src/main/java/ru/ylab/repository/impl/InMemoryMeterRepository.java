package ru.ylab.repository.impl;

import lombok.RequiredArgsConstructor;
import ru.ylab.entity.Meter;
import ru.ylab.repository.MeterRepository;

import java.util.*;

@RequiredArgsConstructor
public class InMemoryMeterRepository implements MeterRepository {
    private static final Map<Long, Set<Meter>> USER_METERS = new HashMap<>();

    @Override
    public Set<Meter> getByUserId(Long userId) {
        return USER_METERS.getOrDefault(userId, new HashSet<>());
    }

    @Override
    public void save(Meter meter) {
        var userMeters = USER_METERS.getOrDefault(meter.getUser().getId(), new HashSet<>());
        userMeters.add(meter);
        USER_METERS.put(meter.getUser().getId(), userMeters);
    }

    public void save(Collection<Meter> meters) {
        meters.forEach(this::save);
    }

    @Override
    public Optional<Meter> findById(Long meterId) {
        return USER_METERS.values().stream()
                .flatMap(Collection::stream)
                .filter(m -> m.getId().equals(meterId))
                .findFirst();
    }
}
