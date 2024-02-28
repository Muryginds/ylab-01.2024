package io.ylab.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * Data Transfer Object (DTO) representing a meter type.
 *
 * <p>This class is used to transfer meter type-related information between different layers of the application,
 * such as between service and controller layers.
 */
@Builder
@Schema(description = "Модель данных типа счетчика")
public record MeterTypeDto(
        Long id,
        String typeName
) {
}
