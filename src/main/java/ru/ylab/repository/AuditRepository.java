package ru.ylab.repository;

import ru.ylab.entity.Event;

import java.time.LocalDateTime;
import java.util.Map;

public interface AuditRepository {
    Map<LocalDateTime, Event> getEventsByUsername(String username);

    void addEvent(String username, Event event);
}
