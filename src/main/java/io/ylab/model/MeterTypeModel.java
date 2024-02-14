package io.ylab.model;

import lombok.Builder;

@Builder
public record MeterTypeModel(
        Long id,
        String typeName
) {
}
