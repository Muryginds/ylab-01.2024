package ru.ylab.service;

import lombok.RequiredArgsConstructor;
import ru.ylab.entity.Meter;
import ru.ylab.entity.User;
import ru.ylab.exception.MeterNotFoundException;
import ru.ylab.dto.MeterDTO;
import ru.ylab.mapper.MeterMapper;
import ru.ylab.repository.MeterRepository;
import ru.ylab.repository.MeterTypeRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

/**
 * The MeterService class provides functionality related to meters.
 * It includes methods for saving new meters, retrieving meters by various criteria, and generating meters for new users.
 * This service interacts with the MeterRepository and MeterTypeRepository.
 */
@RequiredArgsConstructor
public class MeterService {
    private static final Random random = new Random();

    private final MeterRepository repository;
    private final MeterTypeRepository meterTypeRepository;

    /**
     * Saves a single meter.
     *
     * @param meter The meter to be saved.
     */
    public void save(Meter meter) {
        repository.save(meter);
    }

    /**
     * Saves a collection of meters.
     *
     * @param meters The collection of meters to be saved.
     */
    public void save(Collection<Meter> meters) {
        repository.save(meters);
    }

    /**
     * Retrieves a meter by its ID.
     *
     * @param meterId The ID of the meter to retrieve.
     * @return Meter representing the retrieved meter.
     * @throws MeterNotFoundException If no meter is found with the given ID.
     */
    public Meter getById(Long meterId) {
        return repository.findById(meterId)
                .orElseThrow(() -> new MeterNotFoundException(meterId));
    }

    /**
     * Retrieves DTOs for all meters associated with a specific user.
     *
     * @param userId The ID of the user for whom meters are retrieved.
     * @return Collection of MeterDTO representing meters associated with the user.
     */
    public Collection<MeterDTO> getMeterDTOsByUserId(Long userId) {
        return MeterMapper.MAPPER.toMeterDTOs(getMetersByUserId(userId));
    }

    /**
     * Retrieves meters associated with a specific user.
     *
     * @param userId The ID of the user for whom meters are retrieved.
     * @return Collection of Meter representing meters associated with the user.
     */
    public Collection<Meter> getMetersByUserId(Long userId) {
        return repository.getByUserId(userId);
    }

    /**
     * Generates meters for a new user.
     *
     * @param user The new user for whom meters are generated.
     */
    public void generateForNewUser(User user) {
        var set = new HashSet<Meter>();
        var meterTypes = meterTypeRepository.getAll();
        for (var type : meterTypes) {
            set.add(Meter.builder()
                    .user(user)
                    .factoryNumber(String.format("%012d", random.nextInt(Integer.MAX_VALUE)))
                    .type(type)
                    .build()
            );
        }
        repository.save(set);
    }
}
