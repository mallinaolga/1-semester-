package com.mipt.olgamallina.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Aspect for logging service-layer method execution.
 * Logs start/end of method execution, arguments, and result (if any).
 */
@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * Logs execution of any method inside the service package and its subpackages.
     * Uses Around advice to log start, end, result, and exceptions.
     */
    @Around("execution(* com.mipt.olgamallina.service..*(..))")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String method = signature.getDeclaringType().getSimpleName() + "." + signature.getName();

        Object[] args = joinPoint.getArgs();
        log.info("START {} args={}", method, Arrays.toString(args));

        try {
            Object result = joinPoint.proceed();

            if (signature.getReturnType().equals(void.class)) {
                log.info("END   {} result=<void>", method);
            } else {
                log.info("END   {} result={}", method, result);
            }

            return result;
        } catch (Throwable ex) {
            log.error("ERROR {} message={}", method, ex.getMessage(), ex);
            throw ex;
        }
    }
}