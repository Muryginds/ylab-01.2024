package ru.ylab.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record MessageDTO(
        @JsonProperty("message")
        String message
) {
}
