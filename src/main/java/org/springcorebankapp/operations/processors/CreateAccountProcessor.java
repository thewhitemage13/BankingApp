package org.thewhitemage13.operations.processors;

import org.thewhitemage13.account.AccountService;
import org.thewhitemage13.operations.ConsoleOperationType;
import org.thewhitemage13.operations.OperationCommandProcessor;
import org.thewhitemage13.user.UserService;

import java.util.Scanner;

public class CreateAccountProcessor implements OperationCommandProcessor {
    private final Scanner scanner;
    private final AccountService accountService;
    private final UserService userService;

    public CreateAccountProcessor(Scanner scanner, UserService userService ,AccountService accountService) {
        this.scanner = scanner;
        this.accountService = accountService;
        this.userService = userService;
    }

    @Override
    public void processOperation() {
        System.out.println("Enter the user id for which to create an account:");
        int userId = Integer.parseInt(scanner.nextLine());
        var user = userService.findUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No such user with id=%S"
                        .formatted(userId)));
        var account =  accountService.createAccount(user);
        user.getAccountList().add(account);

        System.out.println("New account created with Id: %s for user: %s"
                .formatted(account.getId(), user.getLogin()));
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_CREATE;
    }
}












