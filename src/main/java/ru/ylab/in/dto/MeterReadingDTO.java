package ru.ylab.in.dto;

import lombok.Builder;

@Builder
public record MeterReadingDTO(
        Long id,
        MeterDTO meterDTO,
        SubmissionDTO submissionDTO,
        Long value
) {
}
