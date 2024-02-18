package io.ylab.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.ylab.dto.response.ErrorResponseDto;
import io.ylab.exception.BaseMonitoringServiceException;
import io.ylab.exception.UserNotAuthorizedException;
import io.ylab.utils.ResponseUtils;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler({
            UserNotAuthorizedException.class,
            AccessDeniedException.class
    })
    public ResponseEntity<ErrorResponseDto> handleUserNotAuthorizedException(Exception e) {
        return getResponseEntity(HttpStatus.FORBIDDEN, prepareErrorResponse(e));
    }

    @ExceptionHandler({
            ConstraintViolationException.class,
            BaseMonitoringServiceException.class,
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class,
            ExpiredJwtException.class,
            InternalAuthenticationServiceException.class
    })
    public ResponseEntity<ErrorResponseDto> handleCustomExceptions(Exception e) {
        return getResponseEntity(HttpStatus.BAD_REQUEST, prepareErrorResponse(e));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleArgumentNotValidationException(MethodArgumentNotValidException e) {
        List<String> errors = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(String.format("%s - %s", fieldName, errorMessage));
        });
        return getResponseEntity(HttpStatus.BAD_REQUEST, prepareErrorResponse(errors));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodValidationException(HandlerMethodValidationException e) {
        List<String> errors = new ArrayList<>();
        e.getAllValidationResults().forEach(error -> {
            String fieldName = error.getMethodParameter().getParameterName();
            error.getResolvableErrors().forEach(message ->
                    errors.add(String.format("%s - %s", fieldName, message.getDefaultMessage()))
            );
        });
        return getResponseEntity(HttpStatus.BAD_REQUEST, prepareErrorResponse(errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception e) {
        var sb = new StringBuilder();
        for (var element : e.getStackTrace()) {
            sb.append(element.toString()).append("\n");
        }
        log.error(sb.toString());
        return getResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, prepareErrorResponse(e));
    }

    private ResponseEntity<ErrorResponseDto> getResponseEntity(HttpStatus status, ErrorResponseDto response) {
        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    private ErrorResponseDto prepareErrorResponse(Exception e) {
        return ResponseUtils.responseWithError(List.of(e.getLocalizedMessage()));
    }

    private ErrorResponseDto prepareErrorResponse(List<String> errors) {
        return ResponseUtils.responseWithError(errors);
    }
}
