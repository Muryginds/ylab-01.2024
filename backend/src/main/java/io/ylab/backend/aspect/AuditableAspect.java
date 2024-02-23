package io.ylab.backend.aspect;

import io.ylab.backend.annotation.Auditable;
import io.ylab.backend.entity.AuditionEvent;
import io.ylab.backend.service.AuditionEventService;
import io.ylab.backend.service.UserService;
import io.ylab.backend.dto.response.UserDto;
import io.ylab.backend.utils.CurrentUserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuditableAspect {
    private final AuditionEventService auditionEventService;
    private final UserService userService;

    @Pointcut("execution(io.ylab.backend.dto.response.UserDto io.ylab.backend.service.AccountService.registerUser(..))" +
            "|| execution(io.ylab.backend.dto.response.UserDto io.ylab.backend.service.LoginService.authorize(..))")
    public void unauthorizedUserMethodPointcut() {
    }

    @Pointcut("@annotation(io.ylab.backend.annotation.Auditable) && execution(* *(..))")
    public void auditableMethod() {
    }

    @AfterReturning("auditableMethod() && !unauthorizedUserMethodPointcut()")
    public void auditableMethodAdvice(JoinPoint joinPoint) {
        var methodName = joinPoint.getSignature().getName();
        log.info("AUDITING " + methodName);
        var type = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(Auditable.class).eventType();
        var user = CurrentUserUtils.getCurrentUser();
        var event = AuditionEvent.builder()
                .user(user)
                .eventType(type)
                .message("Method '%s' was called".formatted(methodName))
                .build();
        auditionEventService.save(event);
    }

    @AfterReturning(value = "auditableMethod() && unauthorizedUserMethodPointcut()", returning = "userDto")
    public void auditableRegisterUserAdvice(JoinPoint joinPoint, UserDto userDto) {
        var methodName = joinPoint.getSignature().getName();
        log.info("AUDITING " + methodName);
        var type = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(Auditable.class).eventType();
        var user = userService.getUserById(userDto.getId());
        var event = AuditionEvent.builder()
                .user(user)
                .eventType(type)
                .message("Method '%s' was called".formatted(methodName))
                .build();
        auditionEventService.save(event);
    }
}
