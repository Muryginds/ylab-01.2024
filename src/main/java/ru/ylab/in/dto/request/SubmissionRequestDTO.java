package ru.ylab.in.dto.request;

import lombok.Builder;

import java.util.Map;

@Builder
public record SubmissionRequestDTO(
        Map<Long, Long> meterReadings
) {
}
