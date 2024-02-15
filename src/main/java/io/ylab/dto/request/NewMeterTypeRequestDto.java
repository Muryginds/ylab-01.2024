package io.ylab.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record NewMeterTypeRequestDto(
        @NotBlank
        @JsonProperty("typeName")
        String typeName
) {
}
