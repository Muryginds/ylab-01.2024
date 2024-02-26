package io.ylab.repository;

import io.ylab.entity.AuditionEvent;
import io.ylab.model.AuditionEventModel;

import java.util.Collection;

/**
 * Repository interface for managing audition event-related data operations.
 */
public interface AuditionEventRepository {

    /**
     * Gets all audition events associated with a specific user.
     *
     * @param userId The ID of the user.
     * @return A collection of audition event models associated with the specified user.
     */
    Collection<AuditionEventModel> getEventsByUserId(Long userId);

    /**
     * Saves a new audition event to the repository.
     *
     * @param auditionEvent The audition event to be saved.
     */
    void save(AuditionEvent auditionEvent);
}
