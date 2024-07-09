package org.thewhitemage13.operations.processors;

import org.thewhitemage13.account.Account;
import org.thewhitemage13.account.AccountService;
import org.thewhitemage13.operations.ConsoleOperationType;
import org.thewhitemage13.operations.OperationCommandProcessor;
import org.thewhitemage13.user.User;
import org.thewhitemage13.user.UserService;

import java.util.Scanner;

public class CloseAccountProcessor implements OperationCommandProcessor {
    private final Scanner scanner;
    private final AccountService accountService;
    private final UserService userService;

    public CloseAccountProcessor(Scanner scanner, AccountService accountService, UserService userService) {
        this.scanner = scanner;
        this.accountService = accountService;
        this.userService = userService;
    }

    @Override
    public void processOperation() {
        System.out.println("Enter account id to close:");
        int accountId = Integer.parseInt(scanner.nextLine());
        Account account = accountService.closeAccount(accountId);

        User user = userService.findUserById(account.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("No such user with id=%s"
                        .formatted(account.getUserId())));
        user.getAccountList().remove(account);
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_CLOSE;
    }
}
