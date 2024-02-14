package io.ylab.repository;

import io.ylab.entity.MeterType;
import io.ylab.model.MeterTypeModel;

import java.util.Collection;
import java.util.Optional;

/**
 * Repository interface for managing meter types-related data operations.
 */
public interface MeterTypeRepository {

    /**
     * Saves a single meter type in the repository.
     *
     * @param meterType The meter type to be saved.
     */
    void save(MeterType meterType);

    /**
     * Saves a collection of meter types in the repository.
     *
     * @param meterTypes The collection of meter types to be saved.
     */
    void save(Collection<MeterType> meterTypes);

    /**
     * Finds a meter type model by its ID in the repository.
     *
     * @param meterTypeId The ID of the meter type.
     * @return An optional containing the found meter type model or empty if not found.
     */
    Optional<MeterTypeModel> findById(Long meterTypeId);

    /**
     * Checks if a meter type with a given name exists in the repository.
     *
     * @param typeName The name of the meter type.
     * @return True if a meter type with the specified name exists, false otherwise.
     */
    boolean checkExistsByName(String typeName);

    /**
     * Gets all meter types stored in the repository.
     *
     * @return A collection of all meter type models.
     */
    Collection<MeterTypeModel> getAll();
}
