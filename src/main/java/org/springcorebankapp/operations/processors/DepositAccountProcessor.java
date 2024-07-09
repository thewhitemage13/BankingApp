package org.springcorebankapp.operations.processors;

import org.springcorebankapp.account.AccountService;
import org.springcorebankapp.operations.ConsoleOperationType;
import org.springcorebankapp.operations.OperationCommandProcessor;
import org.springframework.stereotype.Component;

import java.util.Scanner;
@Component
public class DepositAccountProcessor implements OperationCommandProcessor {
    private final Scanner scanner;
    private AccountService accountService;

    public DepositAccountProcessor(Scanner scanner, AccountService accountService) {
        this.scanner = scanner;
        this.accountService = accountService;
    }

    @Override
    public void processOperation() {
        System.out.println("Enter account id:");
        int accountId = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter amount to deposit:");
        int amountToDeposit = Integer.parseInt(scanner.nextLine());
        accountService.depositAccount(accountId, amountToDeposit);
        System.out.println("Successfully deposited amount=%s to account id=%s"
                .formatted(amountToDeposit, accountId));
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_DEPOSIT;
    }
}
