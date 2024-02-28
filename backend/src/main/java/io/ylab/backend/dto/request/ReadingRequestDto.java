package io.ylab.backend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
@Schema(description = "Модель данных показаний счетчиков")
public record ReadingRequestDto(
        @Positive
        @NotEmpty
        @JsonProperty("meterId")
        Long meterId,
        @Positive
        @NotEmpty
        @JsonProperty("value")
        Long value
) {
}
