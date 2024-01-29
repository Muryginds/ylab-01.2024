package ru.ylab.in.dto;

import lombok.Builder;

/**
 * Data Transfer Object (DTO) representing a meter.
 *
 * <p>This class is used to transfer meter-related information between different layers of the application,
 * such as between service and controller layers.
 */
@Builder
public record MeterDTO(
        Long id,
        String factoryNumber,
        UserDTO userDTO,
        MeterTypeDTO typeDTO
) {
}
