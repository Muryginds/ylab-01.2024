package io.ylab.dto.response;

import lombok.Builder;

/**
 * Data Transfer Object (DTO) representing a meter type.
 *
 * <p>This class is used to transfer meter type-related information between different layers of the application,
 * such as between service and controller layers.
 */
@Builder
public record MeterTypeDto(
        Long id,
        String typeName
) {
}
