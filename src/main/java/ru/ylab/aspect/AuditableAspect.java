package ru.ylab.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import ru.ylab.annotation.Auditable;
import ru.ylab.dto.response.UserDTO;
import ru.ylab.entity.AuditionEvent;
import ru.ylab.entity.User;
import ru.ylab.exception.UserNotAuthorizedException;
import ru.ylab.service.AuditionEventService;
import ru.ylab.service.UserService;
import ru.ylab.utils.ApplicationComponentsFactory;

@Aspect
@Slf4j
public class AuditableAspect {
    private final AuditionEventService auditionEventService =
            ApplicationComponentsFactory.getAuditionEventService();
    private final UserService userService = ApplicationComponentsFactory.getUserService();

    @Pointcut("execution(ru.ylab.dto.response.UserDTO ru.ylab.service.LoginService.registerUser(..))")
    public void registerUserPointcut() {
    }

    @Pointcut("@annotation(ru.ylab.annotation.Auditable) && execution(* *(..))")
    public void auditableMethod() {
    }

    @Around("auditableMethod() && !registerUserPointcut()")
    public Object auditableMethodAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        var methodName = joinPoint.getSignature().getName();
        log.info("AUDITING " + methodName);
        var type = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(Auditable.class).eventType();
        User user;
        Object proceed;
        try {
            user = userService.getCurrentUser();
            proceed = joinPoint.proceed();
        } catch (UserNotAuthorizedException ex) {
            proceed = joinPoint.proceed();
            user = userService.getCurrentUser();
        }
        var event = AuditionEvent.builder()
                .user(user)
                .eventType(type)
                .message("Method '%s' was called".formatted(methodName))
                .build();
        auditionEventService.save(event);
        return proceed;
    }

    @Around("auditableMethod() && registerUserPointcut()")
    public Object auditableRegisterUserAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        var methodName = joinPoint.getSignature().getName();
        log.info("AUDITING " + methodName);
        var type = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(Auditable.class).eventType();
        var proceed = (UserDTO)joinPoint.proceed();
        var user = userService.getUserById(proceed.id());
        var event = AuditionEvent.builder()
                .user(user)
                .eventType(type)
                .message("Method '%s' was called".formatted(methodName))
                .build();
        auditionEventService.save(event);
        return proceed;
    }
}
