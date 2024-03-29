package io.ylab.logging.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class LoggableAspect {
    @Pointcut("@annotation(io.ylab.logging.annotation.Loggable) && execution(* *(..))")
    public void loggableMethod() {
    }

    @Around("loggableMethod()")
    public Object loggableMethodAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        var methodName = (joinPoint.getSignature()).getName();
        log.warn("Logging method execution '%s'".formatted(methodName));
        var start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        var totalExecutionTime = System.currentTimeMillis() - start;
        log.warn("Logging method '%s' finished. Execution time: %s ms"
                .formatted(methodName, totalExecutionTime)
        );
        return proceed;
    }
}
