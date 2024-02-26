package io.ylab.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.ylab.dto.request.UserRegistrationRequestDto;
import io.ylab.dto.response.ErrorResponseDto;
import io.ylab.dto.response.UserDto;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Controller responsible for account-related operations and interactions.
 */
@Tag(name = "Account")
public interface AccountController {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Аккаунт зарегистрирован",
                    content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @Operation(summary = "Регистрация пользователя")
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    UserDto register(@Valid @RequestBody UserRegistrationRequestDto requestDto);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные получены",
                    content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "403", description = "Пользователь не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @Operation(summary = "Получение данных текущего пользователя")
    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    UserDto getCurrentUser();
}
