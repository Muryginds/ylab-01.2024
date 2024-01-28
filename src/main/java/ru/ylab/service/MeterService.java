package ru.ylab.service;

import lombok.RequiredArgsConstructor;
import ru.ylab.entity.Meter;
import ru.ylab.entity.User;
import ru.ylab.exception.MeterNotFoundException;
import ru.ylab.in.dto.MeterDTO;
import ru.ylab.mapper.MeterMapper;
import ru.ylab.repository.MeterRepository;
import ru.ylab.repository.MeterTypeRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

@RequiredArgsConstructor
public class MeterService {
    private static final Random random = new Random();

    private final MeterRepository repository;
    private final MeterTypeRepository meterTypeRepository;

    public void save(Meter meter) {
        repository.save(meter);
    }

    public void save(Collection<Meter> meters) {
        repository.save(meters);
    }

    public Meter getById(Long meterId) {
        return repository.findById(meterId)
                .orElseThrow(() -> new MeterNotFoundException(meterId));
    }

    public Collection<MeterDTO> getMeterDTOsByUserId(Long userId) {
        return MeterMapper.MAPPER.toMeterDTOs(getMetersByUserId(userId));
    }

    public Collection<Meter> getMetersByUserId(Long userId) {
        return repository.getByUserId(userId);
    }

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
