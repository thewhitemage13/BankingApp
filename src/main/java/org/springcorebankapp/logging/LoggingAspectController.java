package org.springcorebankapp.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspectController {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspectController.class);

    @Pointcut("execution(* org.springcorebankapp.account.AccountController.*(..)) || execution(* org.springcorebankapp.user.UserController.*(..))")
    public void controllerMethodsPointcut() {
    }

    @Before("controllerMethodsPointcut()")
    public void logBefore(JoinPoint joinPoint) {
        logger.debug("Method call: {} with arguments: {}",
                joinPoint.getSignature().toShortString(),
                joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "controllerMethodsPointcut()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("The {} method executed successfully. Result: {}",
                joinPoint.getSignature().toShortString(),
                result);
    }

    @AfterThrowing(pointcut = "controllerMethodsPointcut()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        logger.error("The {} method raised an exception: {}",
                joinPoint.getSignature().toShortString(),
                exception.getMessage(),
                exception);
    }
}
