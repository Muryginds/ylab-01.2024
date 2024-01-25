package ru.ylab.repository;

import ru.ylab.entity.AuditionEvent;

import java.util.Collection;

public interface AuditRepository {
    Collection<AuditionEvent> getEventsByUserId(Long userId);

    void addEvent(Long userId, AuditionEvent auditionEvent);
}
