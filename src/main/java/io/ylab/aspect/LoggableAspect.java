package io.ylab.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
@Slf4j
public class LoggableAspect {
    @Pointcut("@annotation(ru.ylab.annotation.Loggable) && execution(* *(..))")
    public void loggableMethod() {
    }

    @Around("loggableMethod()")
    public Object loggableMethodAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        var methodName = (joinPoint.getSignature()).getName();
        log.info("Logging method execution '%s'".formatted(methodName));
        var start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        var totalExecutionTime = System.currentTimeMillis() - start;
        log.info("Logging method '%s' finished. Execution time: %s ms"
                .formatted(methodName, totalExecutionTime)
        );
        return proceed;
    }
}
