package ru.ylab.in.dto.request;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record SubmissionByDateRequestDTO(
            LocalDate date,
            Long userId
) {
}
