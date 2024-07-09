package org.springcorebankapp.operations.processors;

import org.springcorebankapp.account.Account;
import org.springcorebankapp.account.AccountService;
import org.springcorebankapp.operations.ConsoleOperationType;
import org.springcorebankapp.operations.OperationCommandProcessor;
import org.springcorebankapp.user.User;
import org.springcorebankapp.user.UserService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Scanner;
@Component
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

        System.out.println("Account successfully closed with id=%s"
                .formatted(accountId));
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_CLOSE;
    }
}
