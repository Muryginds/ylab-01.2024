package ru.ylab.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.ylab.entity.MeterReading;
import ru.ylab.entity.Submission;
import ru.ylab.dto.response.SubmissionDTO;
import ru.ylab.entity.User;
import ru.ylab.model.SubmissionModel;

import java.util.Set;

@Mapper(uses = {UserMapper.class, MeterReadingMapper.class})
public interface SubmissionMapper {
    SubmissionMapper MAPPER = Mappers.getMapper(SubmissionMapper.class);

    @Mapping(target = "userDTO", source = "submission.user")
    SubmissionDTO toSubmissionDTO(Submission submission, Set<MeterReading> readings);

    @Mapping(target = "id", source = "submissionModel.id")
    Submission toSubmission(SubmissionModel submissionModel, User user);

    @Mapping(target = "userId", source = "submission.user.id")
    SubmissionModel toSubmissionModel(Submission submission);
}
