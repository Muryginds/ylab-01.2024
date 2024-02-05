package ru.ylab.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.ylab.entity.Meter;
import ru.ylab.entity.MeterReading;
import ru.ylab.dto.MeterReadingDTO;
import ru.ylab.entity.Submission;
import ru.ylab.model.MeterReadingModel;

import java.util.Set;

@Mapper(uses = {SubmissionMapper.class, MeterMapper.class})
public interface MeterReadingMapper {
    MeterReadingMapper MAPPER = Mappers.getMapper(MeterReadingMapper.class);

    @Mapping(target = "submissionDTO", source = "submission")
    @Mapping(target = "meterDTO", source = "meter")
    MeterReadingDTO toMeterReadingDTO(MeterReading meterReading);

    Set<MeterReadingDTO> toMeterReadingDTOSet(Set<MeterReading> meterReadings);

    @Mapping(target = "id", source = "meterReadingModel.id")
    MeterReading toMeterReading(MeterReadingModel meterReadingModel, Meter meter, Submission submission);

    @Mapping(target = "submissionId", source = "meterReading.submission.id")
    @Mapping(target = "meterId", source = "meterReading.meter.id")
    MeterReadingModel toMeterReadingModel(MeterReading meterReading);
}
