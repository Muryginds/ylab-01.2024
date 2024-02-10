package ru.ylab.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record ErrorDTO(
        @JsonProperty("error")
        String error
) {
}
