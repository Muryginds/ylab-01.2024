package io.ylab.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.ylab.backend.dto.request.UserAuthorizationRequestDto;
import io.ylab.backend.dto.response.ErrorResponseDto;
import io.ylab.backend.dto.response.MessageDto;
import io.ylab.backend.dto.response.UserDto;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Controller responsible for handling user authentication and registration.
 */
@Tag(name = "Authorization")
public interface LoginController {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь авторизован",
                    content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @Operation(summary = "Авторизация пользователя")
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    UserDto authorize(@Valid @RequestBody UserAuthorizationRequestDto requestDto);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь закончил сесиию",
                    content = @Content(schema = @Schema(implementation = MessageDto.class))),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @Operation(summary = "Завершение сессии пользователя")
    @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    MessageDto logout();
}
