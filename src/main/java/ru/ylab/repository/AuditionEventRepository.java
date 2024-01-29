package ru.ylab.repository;

import ru.ylab.entity.AuditionEvent;

import java.util.Collection;

/**
 * Repository interface for managing audition event-related data operations.
 */
public interface AuditionEventRepository {

    /**
     * Gets all audition events associated with a specific user.
     *
     * @param userId The ID of the user.
     * @return A collection of audition events associated with the specified user.
     */
    Collection<AuditionEvent> getEventsByUserId(Long userId);

    /**
     * Adds a new audition event to the repository.
     *
     * @param auditionEvent The audition event to be added.
     */
    void addEvent(AuditionEvent auditionEvent);
}
