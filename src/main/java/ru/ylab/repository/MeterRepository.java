package ru.ylab.repository;

import ru.ylab.entity.Meter;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

/**
 * Repository interface for managing meter-related data operations.
 */
public interface MeterRepository {

    /**
     * Gets all meters associated with a specific user.
     *
     * @param userId The ID of the user.
     * @return A set of meters associated with the specified user.
     */
    Set<Meter> getByUserId(Long userId);

    /**
     * Saves a single meter in the repository.
     *
     * @param meter The meter to be saved.
     */
    void save(Meter meter);

    /**
     * Saves a collection of meters in the repository.
     *
     * @param meters The collection of meters to be saved.
     */
    void save(Collection<Meter> meters);

    /**
     * Finds a meter by its ID in the repository.
     *
     * @param meterId The ID of the meter.
     * @return An optional containing the found meter or empty if not found.
     */
    Optional<Meter> findById(Long meterId);
}
