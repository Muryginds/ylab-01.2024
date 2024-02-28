package io.ylab.audition.aspect;


import io.ylab.audition.annotation.Auditable;
import io.ylab.audition.repository.EventRepository;
import io.ylab.commons.entity.AuditionEvent;
import io.ylab.commons.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class AuditableAspect {
    private final EventRepository eventRepository;

    @Pointcut("@annotation(io.ylab.audition.annotation.Auditable) && execution(* *(..))")
    public void auditableMethod() {
    }

    @AfterReturning("auditableMethod()")
    public void auditableMethodAdvice(JoinPoint joinPoint) {
        var methodName = joinPoint.getSignature().getName();
        var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.warn("AUDITING {} by {}", methodName, user.getName());
        var type = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(Auditable.class).eventType();
        var params = Arrays.stream(joinPoint.getArgs())
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        var event = AuditionEvent.builder()
                .user(user)
                .eventType(type)
                .message("Method '%s' was called with params %s".formatted(methodName, params))
                .build();
        eventRepository.save(event);
    }
}
