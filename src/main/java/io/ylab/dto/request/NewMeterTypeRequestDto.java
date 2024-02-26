package io.ylab.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(description = "Модель данных типа счетчика")
public record NewMeterTypeRequestDto(
        @NotBlank
        @JsonProperty("typeName")
        String typeName
) {
}
