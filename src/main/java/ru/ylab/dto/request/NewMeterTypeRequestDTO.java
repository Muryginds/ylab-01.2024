package ru.ylab.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record NewMeterTypeRequestDTO(
        @NotBlank
        @JsonProperty("typeName")
        String typeName
) {
}