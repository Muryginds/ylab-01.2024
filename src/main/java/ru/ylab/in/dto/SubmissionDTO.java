package ru.ylab.in.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record SubmissionDTO(
        Long id,
        UserDTO userDTO,
        LocalDate date
) {
}
