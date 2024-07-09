package org.thewhitemage13.operations.processors;

import jdk.dynalink.Operation;
import org.thewhitemage13.operations.ConsoleOperationType;
import org.thewhitemage13.operations.OperationCommandProcessor;
import org.thewhitemage13.user.User;
import org.thewhitemage13.user.UserService;

import java.util.Scanner;

public class CreateUserProcessor implements OperationCommandProcessor {
    private final Scanner scanner;
    private final UserService userService;

    public CreateUserProcessor(Scanner scanner, UserService userService) {
        this.scanner = scanner;
        this.userService = userService;
    }

    @Override
    public void processOperation() {
        System.out.println("Enter login for new user: ");
        String login = scanner.nextLine();
        User user = userService.createUser(login);
        System.out.println("User created: " + user.toString());
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.USER_CREATE;
    }
}
