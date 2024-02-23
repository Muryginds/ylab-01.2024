package io.ylab.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * Data Transfer Object (DTO) representing a meter.
 *
 * <p>This class is used to transfer meter-related information between different layers of the application,
 * such as between service and controller layers.
 */
@Builder
@Schema(description = "Модель данных счетчика")
public record MeterDto(
        Long id,
        String factoryNumber,
        UserDto userDTO,
        MeterTypeDto meterTypeDTO
) {
}
