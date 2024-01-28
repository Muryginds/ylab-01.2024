package ru.ylab.repository;

import ru.ylab.entity.AuditionEvent;

import java.util.Collection;

public interface AuditionEventRepository {
    Collection<AuditionEvent> getEventsByUserId(Long userId);

    void addEvent(AuditionEvent auditionEvent);
}
