package io.ylab.backend.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.ylab.backend.exception.BaseMonitoringServiceException;
import io.ylab.backend.exception.NoPermissionException;
import io.ylab.backend.exception.UserNotAuthorizedException;
import io.ylab.backend.utils.ResponseUtils;
import io.ylab.backend.dto.response.ErrorResponseDto;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler({
            UserNotAuthorizedException.class
    })
    public ResponseEntity<ErrorResponseDto> handleUserNotAuthorizedException(Exception e) {
        return getResponseEntity(HttpStatus.UNAUTHORIZED, prepareErrorResponse(e));
    }

    @ExceptionHandler({
            NoPermissionException.class
    })
    public ResponseEntity<ErrorResponseDto> handleNoPermissionException(Exception e) {
        return getResponseEntity(HttpStatus.FORBIDDEN, prepareErrorResponse(e));
    }

    @ExceptionHandler({
            ConstraintViolationException.class,
            BaseMonitoringServiceException.class,
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class,
            ExpiredJwtException.class,
            AuthenticationException.class
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
        log.error(e.getMessage());
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
