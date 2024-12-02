package org.springcorebankapp.exception;

/**
 * Custom exception thrown when a user is not found in the system.
 * <p>
 * This exception is thrown in cases where a user cannot be found in the system,
 * for example, when trying to retrieve user details or perform actions on a non-existent user.
 * </p>
 * <p>
 * It extends {@link RuntimeException}, making it an unchecked exception.
 * </p>
 *
 * @author Mukhammed Lolo
 * @version 1.0.0
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Default constructor.
     * <p>
     * This constructor creates a new instance of {@link UserNotFoundException} with no message or cause.
     * </p>
     */
    public UserNotFoundException() {
        super();
    }

    /**
     * Constructor with a custom error message.
     * <p>
     * This constructor creates a new instance of {@link UserNotFoundException} with the provided message.
     * </p>
     *
     * @param message the detail message explaining the reason for the exception
     */
    public UserNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor with a custom error message and a cause.
     * <p>
     * This constructor creates a new instance of {@link UserNotFoundException} with the provided message and cause.
     * </p>
     *
     * @param message the detail message explaining the reason for the exception
     * @param cause the cause of the exception (which is saved for later retrieval by the {@link Throwable#getCause()} method)
     */
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with a cause.
     * <p>
     * This constructor creates a new instance of {@link UserNotFoundException} with the provided cause.
     * </p>
     *
     * @param cause the cause of the exception (which is saved for later retrieval by the {@link Throwable#getCause()} method)
     */
    public UserNotFoundException(Throwable cause) {
        super(cause);
    }
}
