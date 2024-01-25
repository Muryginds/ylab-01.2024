package ru.ylab.repository;

import ru.ylab.entity.AuditionEvent;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class InMemoryAuditRepository implements AuditRepository {
    private static final Map<Long, TreeMap<LocalDateTime, AuditionEvent>> EVENTS = init();

    private static Map<Long, TreeMap<LocalDateTime, AuditionEvent>> init() {
        return new HashMap<>();
    }

    private static TreeMap<LocalDateTime, AuditionEvent> getUserEventsMap(Long userId) {
        return EVENTS.getOrDefault(userId, new TreeMap<>());
    }

    @Override
    public Collection<AuditionEvent> getEventsByUserId(Long userId) {
        return getUserEventsMap(userId).values();
    }

    @Override
    public void addEvent(Long userId, AuditionEvent auditionEvent) {
        var events = getUserEventsMap(userId);
        events.put(auditionEvent.getDate(), auditionEvent);
    }
}
