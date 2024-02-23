package io.ylab.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.ylab.backend.dto.response.AuditionEventDto;
import io.ylab.backend.dto.response.ErrorResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@ApiResponse(responseCode = "401", description = "Пользователь не авторизован",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
@ApiResponse(responseCode = "403", description = "У пользователя недостаточно прав",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
@Tag(name = "Audition")
public interface AuditionEventController {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "События получены",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AuditionEventDto.class)))),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @Operation(summary = "Получение списка событий")
    @GetMapping(value = "/all")
    Set<AuditionEventDto> getEvents(@RequestParam(name = "userId") long userId);
}
