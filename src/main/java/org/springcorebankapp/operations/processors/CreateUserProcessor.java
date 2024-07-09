package org.springcorebankapp.operations.processors;

import org.springcorebankapp.operations.ConsoleOperationType;
import org.springcorebankapp.operations.OperationCommandProcessor;
import org.springcorebankapp.user.User;
import org.springcorebankapp.user.UserRepository;
import org.springcorebankapp.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Scanner;
@Component
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
