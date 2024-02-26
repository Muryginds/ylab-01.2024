package io.ylab.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import io.ylab.entity.Meter;
import io.ylab.entity.MeterReading;
import io.ylab.dto.response.MeterReadingDto;
import io.ylab.entity.Submission;
import io.ylab.model.MeterReadingModel;

import java.util.Set;

@Mapper(componentModel = "spring", uses = {UserMapper.class, MeterMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface MeterReadingMapper {

    @Mapping(target = "meterDTO", source = "meter")
    MeterReadingDto toMeterReadingDTO(MeterReading meterReading);

    Set<MeterReadingDto> toMeterReadingDTOSet(Set<MeterReading> meterReadings);

    @Mapping(target = "id", source = "meterReadingModel.id")
    MeterReading toMeterReading(MeterReadingModel meterReadingModel, Meter meter, Submission submission);

    @Mapping(target = "submissionId", source = "meterReading.submission.id")
    @Mapping(target = "meterId", source = "meterReading.meter.id")
    MeterReadingModel toMeterReadingModel(MeterReading meterReading);
}
