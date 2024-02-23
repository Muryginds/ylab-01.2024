package io.ylab.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.ylab.backend.dto.request.NewMeterTypeRequestDto;
import io.ylab.backend.dto.response.ErrorResponseDto;
import io.ylab.backend.dto.response.MessageDto;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Controller class for handling meter type-related operations and interactions.
 *
 * <p>This class acts as an interface between the user interface and the underlying business logic
 * related to meter types.
 */
@ApiResponse(responseCode = "401", description = "Пользователь не авторизован",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
@Tag(name = "Meter type")
public interface MeterTypeController {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Сохранение прошло успешно",
                    content = @Content(schema = @Schema(implementation = MessageDto.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @Operation(summary = "Добавление нового типа счетчика")
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    MessageDto save(@Valid @RequestBody NewMeterTypeRequestDto requestDto);
}
