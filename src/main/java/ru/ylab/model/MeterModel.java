package ru.ylab.model;

import lombok.Builder;

@Builder
public record MeterModel (
        Long id,
        String factoryNumber,
        Long userId,
        Long typeId) {
}
