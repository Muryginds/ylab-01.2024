package ru.ylab.in.dto;

import lombok.Builder;

@Builder
public record MeterDTO(
        Long id,
        String factoryNumber,
        UserDTO userDTO,
        MeterTypeDTO typeDTO
) {
}
