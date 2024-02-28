package io.ylab.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Модель данных отправки сообщения")
public record MessageDto(
        @JsonProperty("message")
        String message
) {
}
