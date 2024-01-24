package ru.ylab.repository;

import ru.ylab.entity.Event;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class InMemoryAuditRepository implements AuditRepository {
    private static final Map<String, TreeMap<LocalDateTime, Event>> EVENTS = init();

    private static Map<String, TreeMap<LocalDateTime, Event>> init() {
        return new HashMap<>();
    }

    @Override
    public Map<LocalDateTime, Event> getEventsByUsername(String username) {
        return EVENTS.getOrDefault(username, new TreeMap<>());
    }

    @Override
    public void addEvent(String username, Event event) {
        var events = getEventsByUsername(username);
        events.put(LocalDateTime.now(), event);
    }
}
