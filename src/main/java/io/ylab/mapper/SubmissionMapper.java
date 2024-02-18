package io.ylab.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import io.ylab.entity.MeterReading;
import io.ylab.entity.Submission;
import io.ylab.dto.response.SubmissionDto;
import io.ylab.entity.User;
import io.ylab.model.SubmissionModel;

import java.util.Set;

@Mapper(componentModel = "spring", uses = {UserMapper.class, MeterReadingMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface SubmissionMapper {

    @Mapping(target = "userDTO", source = "submission.user")
    SubmissionDto toSubmissionDTO(Submission submission, Set<MeterReading> readings);

    @Mapping(target = "id", source = "submissionModel.id")
    Submission toSubmission(SubmissionModel submissionModel, User user);

    @Mapping(target = "userId", source = "submission.user.id")
    SubmissionModel toSubmissionModel(Submission submission);
}
