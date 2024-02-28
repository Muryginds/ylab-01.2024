package io.ylab.backend.model;

import lombok.Builder;

@Builder
public record MeterModel (
        Long id,
        String factoryNumber,
        Long userId,
        Long meterTypeId) {
}
