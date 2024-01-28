package ru.ylab.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.ylab.entity.Submission;
import ru.ylab.in.dto.SubmissionDTO;

import java.util.Collection;

@Mapper(uses = {UserMapper.class})
public interface SubmissionMapper {
    SubmissionMapper MAPPER = Mappers.getMapper(SubmissionMapper.class);

    @Mapping(target = "userDTO", source = "user")
    SubmissionDTO toSubmissionDTO(Submission submission);

    Collection<SubmissionDTO> toSubmissionDTOs(Collection<Submission> submission);
}
