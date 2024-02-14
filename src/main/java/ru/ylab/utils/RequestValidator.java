package ru.ylab.utils;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import ru.ylab.exception.DtoValidationException;

import java.util.stream.Collectors;

public class RequestValidator {
    private final ValidatorFactory validatorFactory = buildValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    private ValidatorFactory buildValidatorFactory() {
        return Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory();
    }

    public <T>void validateRequest(T t) {
        var violations = validator.validate(t);
        if (violations.isEmpty()) {
            return;
        }
        var exceptionString = violations.stream()
                .map(v -> "'%s' %s".formatted(v.getPropertyPath(), v.getMessage()))
                .collect(Collectors.joining(", "));
        throw new DtoValidationException(exceptionString);
    }
}
