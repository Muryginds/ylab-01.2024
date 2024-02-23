package io.ylab.backend.model;

import lombok.Builder;

@Builder
public record MeterTypeModel(
        Long id,
        String typeName
) {
}
