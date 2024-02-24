package io.ylab.backend.aspect;

import io.ylab.backend.annotation.Auditable;
import io.ylab.backend.entity.AuditionEvent;
import io.ylab.backend.service.AuditionEventService;
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

    @Pointcut("@annotation(io.ylab.backend.annotation.Auditable) && execution(* *(..))")
    public void auditableMethod() {
    }

    @AfterReturning("auditableMethod()")
    public void auditableMethodAdvice(JoinPoint joinPoint) {
        var methodName = joinPoint.getSignature().getName();
        log.warn("AUDITING " + methodName);
        var type = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(Auditable.class).eventType();
        var user = CurrentUserUtils.getCurrentUser();
        var event = AuditionEvent.builder()
                .user(user)
                .eventType(type)
                .message("Method '%s' was called".formatted(methodName))
                .build();
        auditionEventService.save(event);
    }
}
