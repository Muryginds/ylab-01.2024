package io.ylab.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "Модель данных сообщений об ошибках")
public record ErrorResponseDto(
        @JsonProperty("errors")
        @Schema(description = "Список ошибок")
        List<String> errors
) {
}