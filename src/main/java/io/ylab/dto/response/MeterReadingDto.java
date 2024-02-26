package io.ylab.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * Data Transfer Object (DTO) representing a meter reading.
 *
 * <p>This class is used to transfer meter reading-related information between different layers of the application,
 * such as between service and controller layers.
 */
@Builder
@Schema(description = "Модель данных показаний счетчиков")
public record MeterReadingDto(
        Long id,
        MeterDto meterDTO,
        Long value
) {
}
