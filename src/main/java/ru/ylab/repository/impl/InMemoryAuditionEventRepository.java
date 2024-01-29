package ru.ylab.repository.impl;

import ru.ylab.entity.AuditionEvent;
import ru.ylab.repository.AuditionEventRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class InMemoryAuditionEventRepository implements AuditionEventRepository {
    private static final Map<Long, TreeMap<LocalDateTime, AuditionEvent>> EVENTS = new HashMap<>();

    private static TreeMap<LocalDateTime, AuditionEvent> getUserEventsMap(Long userId) {
        return EVENTS.getOrDefault(userId, new TreeMap<>());
    }

    @Override
    public Collection<AuditionEvent> getEventsByUserId(Long userId) {
        return getUserEventsMap(userId).values();
    }

    @Override
    public void addEvent(AuditionEvent auditionEvent) {
        var userId = auditionEvent.getUser().getId();
        var events = getUserEventsMap(userId);
        events.put(auditionEvent.getDate(), auditionEvent);
        EVENTS.put(userId, events);
    }
}
