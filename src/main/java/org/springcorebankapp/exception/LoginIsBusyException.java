package org.springcorebankapp.exception;

public class LoginIsBusyException extends RuntimeException {

    public LoginIsBusyException() {
        super();
    }

    public LoginIsBusyException(String message) {
        super(message);
    }

    public LoginIsBusyException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginIsBusyException(Throwable cause) {
        super(cause);
    }
}
