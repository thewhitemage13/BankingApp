package org.springcorebankapp.exception;

/**
 * Custom exception thrown when a login is already taken or in use.
 * <p>
 * This exception is thrown in cases where a user attempts to register or log in
 * using a login that is already associated with an existing account in the system.
 * </p>
 * <p>
 * It extends {@link RuntimeException}, making it an unchecked exception.
 * </p>
 *
 * @author Mukhammed Lolo
 * @version 1.0.0
 */
public class LoginIsBusyException extends RuntimeException {

    /**
     * Default constructor.
     * <p>
     * This constructor creates a new instance of {@link LoginIsBusyException} with no message or cause.
     * </p>
     */
    public LoginIsBusyException() {
        super();
    }

    /**
     * Constructor with a custom error message.
     * <p>
     * This constructor creates a new instance of {@link LoginIsBusyException} with the provided message.
     * </p>
     *
     * @param message the detail message explaining the reason for the exception
     */
    public LoginIsBusyException(String message) {
        super(message);
    }

    /**
     * Constructor with a custom error message and a cause.
     * <p>
     * This constructor creates a new instance of {@link LoginIsBusyException} with the provided message and cause.
     * </p>
     *
     * @param message the detail message explaining the reason for the exception
     * @param cause the cause of the exception (which is saved for later retrieval by the {@link Throwable#getCause()} method)
     */
    public LoginIsBusyException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with a cause.
     * <p>
     * This constructor creates a new instance of {@link LoginIsBusyException} with the provided cause.
     * </p>
     *
     * @param cause the cause of the exception (which is saved for later retrieval by the {@link Throwable#getCause()} method)
     */
    public LoginIsBusyException(Throwable cause) {
        super(cause);
    }
}
