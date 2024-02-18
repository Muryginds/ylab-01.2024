package io.ylab.service;

import io.ylab.entity.Meter;
import io.ylab.entity.User;
import io.ylab.exception.MeterNotFoundException;
import io.ylab.mapper.MeterMapper;
import io.ylab.repository.MeterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

/**
 * The MeterService class provides functionality related to meters.
 * It includes methods for saving new meters, retrieving meters by various criteria, and generating meters for new users.
 * This service interacts with the MeterRepository and MeterTypeRepository.
 */
@Service
@RequiredArgsConstructor
public class MeterService {
    private static final Random random = new Random();

    private final MeterRepository meterRepository;
    private final MeterTypeService meterTypeService;
    private final UserService userService;
    private final MeterMapper meterMapper;

    /**
     * Saves a single meter.
     *
     * @param meter The meter to be saved.
     */
    public void save(Meter meter) {
        meterRepository.save(meter);
    }

    /**
     * Saves a collection of meters.
     *
     * @param meters The collection of meters to be saved.
     */
    public void save(Collection<Meter> meters) {
        meterRepository.save(meters);
    }

    /**
     * Retrieves a meter by its ID.
     *
     * @param meterId The ID of the meter to retrieve.
     * @return Meter representing the retrieved meter.
     * @throws MeterNotFoundException If no meter is found with the given ID.
     */
    public Meter getById(Long meterId) {
        var meterModel =  meterRepository.findById(meterId)
                .orElseThrow(() -> new MeterNotFoundException(meterId));
        var meterType = meterTypeService.getMeterTypeById(meterModel.meterTypeId());
        var user = userService.getUserById(meterModel.userId());
        return meterMapper.toMeter(meterModel, meterType, user);
    }

    /**
     * Retrieves meters associated with a specific user.
     *
     * @param userId The ID of the user for whom meters are retrieved.
     * @return Collection of Meter representing meters associated with the user.
     */
    public Collection<Meter> getMetersByUserId(Long userId) {
        var user = userService.getUserById(userId);
        var meterModels = meterRepository.getByUserId(userId);
        var collection = new HashSet<Meter>();
        for (var meterModel : meterModels) {
            var meterType = meterTypeService.getMeterTypeById(meterModel.meterTypeId());
            collection.add(meterMapper.toMeter(meterModel, meterType, user));
        }
        return collection;
    }

    /**
     * Generates meters for a new user.
     *
     * @param user The new user for whom meters are generated.
     */
    public void generateForNewUser(User user) {
        var set = new HashSet<Meter>();
        var meterTypesModels = meterTypeService.getAll();
        for (var typeModel : meterTypesModels) {
            set.add(Meter.builder()
                    .user(user)
                    .factoryNumber(String.format("%012d", random.nextInt(Integer.MAX_VALUE)))
                    .meterType(meterTypeService.getMeterTypeById(typeModel.getId()))
                    .build()
            );
        }
        meterRepository.save(set);
    }
}
