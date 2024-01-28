package ru.ylab.in.dto;

import lombok.Builder;

@Builder
public record MeterTypeDTO(
        Long id,
        String typeName
) {
}
