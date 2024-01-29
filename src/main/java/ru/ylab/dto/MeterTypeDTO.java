package ru.ylab.dto;

import lombok.Builder;

/**
 * Data Transfer Object (DTO) representing a meter type.
 *
 * <p>This class is used to transfer meter type-related information between different layers of the application,
 * such as between service and controller layers.
 */
@Builder
public record MeterTypeDTO(
        Long id,
        String typeName
) {
}
