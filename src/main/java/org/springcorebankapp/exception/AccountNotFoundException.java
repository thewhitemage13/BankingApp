package org.springcorebankapp.exception;

/**
 * Custom exception thrown when an account is not found.
 * <p>
 * This exception is thrown in cases where an account is not found in the system,
 * such as when attempting to retrieve or manipulate an account that doesn't exist.
 * </p>
 * <p>
 * It extends {@link RuntimeException}, making it an unchecked exception.
 * </p>
 *
 * @author Mukhammed Lolo
 * @version 1.0.0
 */
public class AccountNotFoundException extends RuntimeException {

    /**
     * Default constructor.
     * <p>
     * This constructor creates a new instance of {@link AccountNotFoundException} with no message or cause.
     * </p>
     */
    public AccountNotFoundException() {
        super();
    }

    /**
     * Constructor with a custom error message.
     * <p>
     * This constructor creates a new instance of {@link AccountNotFoundException} with the provided message.
     * </p>
     *
     * @param message the detail message explaining the reason for the exception
     */
    public AccountNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor with a custom error message and a cause.
     * <p>
     * This constructor creates a new instance of {@link AccountNotFoundException} with the provided message and cause.
     * </p>
     *
     * @param message the detail message explaining the reason for the exception
     * @param cause the cause of the exception (which is saved for later retrieval by the {@link Throwable#getCause()} method)
     */
    public AccountNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with a cause.
     * <p>
     * This constructor creates a new instance of {@link AccountNotFoundException} with the provided cause.
     * </p>
     *
     * @param cause the cause of the exception (which is saved for later retrieval by the {@link Throwable#getCause()} method)
     */
    public AccountNotFoundException(Throwable cause) {
        super(cause);
    }
}
