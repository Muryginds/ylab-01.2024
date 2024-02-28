package io.ylab.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.ylab.backend.dto.request.NewReadingsSubmissionRequestDto;
import io.ylab.backend.dto.response.ErrorResponseDto;
import io.ylab.backend.dto.response.MessageDto;
import io.ylab.backend.dto.response.SubmissionDto;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Set;

/**
 * Controller class for handling submission-related operations and interactions.
 *
 * <p>This class acts as an interface between the user interface and the underlying business logic
 * related to user submissions.
 */
@ApiResponse(responseCode = "401", description = "Пользователь не авторизован",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
@ApiResponse(responseCode = "403", description = "У пользователя недостаточно прав",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
@Tag(name = "Submission")
public interface SubmissionController {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Показания получены",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = SubmissionDto.class)))),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @Operation(summary = "Получение списка показаний")
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    Set<SubmissionDto> getAllSubmissionDTOs(@RequestParam(name = "userId") long userId);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Показание получено",
                    content = @Content(schema = @Schema(implementation = SubmissionDto.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @Operation(summary = "Получение показания")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    SubmissionDto getSubmissionDTO(@RequestParam(name = "date", required = false) LocalDate date,
                                   @RequestParam(name = "userId", required = false) Long userId);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Сохранение прошло успешно",
                    content = @Content(schema = @Schema(implementation = MessageDto.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @Operation(summary = "Добавление новых показаний")
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    MessageDto saveNewSubmission(@Valid @RequestBody NewReadingsSubmissionRequestDto requestDto);
}
