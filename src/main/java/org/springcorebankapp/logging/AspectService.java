package org.springcorebankapp.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging method calls, their arguments, return values, and exceptions
 * in the service layer of the SpringCoreBankApp project.
 * <p>
 * This aspect is applied to methods in the {@link org.springcorebankapp.account} and
 * {@link org.springcorebankapp.user} packages, logging method executions with their input arguments,
 * return values, and any exceptions that occur.
 * </p>
 * <p>
 * The logging levels used are:
 * <ul>
 *   <li>DEBUG for method calls and input arguments</li>
 *   <li>INFO for successful method executions with results</li>
 *   <li>ERROR for methods that raise exceptions</li>
 * </ul>
 *
 * @author Mukhammed Lolo
 * @version 1.0.0
 */
@Aspect
@Component
public class AspectService {
    private static final Logger logger = LoggerFactory.getLogger(AspectService.class);

    /**
     * Pointcut definition for all methods in the {@link org.springcorebankapp.account} and
     * {@link org.springcorebankapp.user} packages.
     * <p>
     * This pointcut is used to intercept method executions within these service packages.
     * </p>
     */
    @Pointcut("execution(* org.springcorebankapp.account.*.*(..)) || execution(* org.springcorebankapp.user.*.*(..)) " +
            "|| execution(* org.springcorebankapp.account.AccountController.*(..)) " +
            "|| execution(* org.springcorebankapp.user.UserController.*(..))")
    public void serviceMethodsPointcut() {
    }

    /**
     * Advice executed before the methods matching the pointcut.
     * <p>
     * This method logs the details of the method being called and its arguments.
     * The log level is DEBUG.
     * </p>
     *
     * @param joinPoint provides information about the intercepted method and its arguments
     */
    @Before("serviceMethodsPointcut()")
    public void logBefore(JoinPoint joinPoint) {
        logger.debug("Method call: {} with arguments: {}",
                joinPoint.getSignature().toShortString(),
                joinPoint.getArgs());
    }

    /**
     * Advice executed after the methods matching the pointcut return successfully.
     * <p>
     * This method logs the return value of the method after it has executed successfully.
     * The log level is INFO.
     * </p>
     *
     * @param joinPoint provides information about the executed method
     * @param result the result returned by the method
     */
    @AfterReturning(pointcut = "serviceMethodsPointcut()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("The {} method executed successfully. Result: {}",
                joinPoint.getSignature().toShortString(),
                result);
    }

    /**
     * Advice executed if a method matching the pointcut throws an exception.
     * <p>
     * This method logs the exception raised by the method. The log level is ERROR.
     * </p>
     *
     * @param joinPoint provides information about the executed method
     * @param exception the exception thrown by the method
     */
    @AfterThrowing(pointcut = "serviceMethodsPointcut()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        logger.error("The {} method raised an exception: {}",
                joinPoint.getSignature().toShortString(),
                exception.getMessage(),
                exception);
    }
}
