package org.springcorebankapp.operations.processors;

import org.springcorebankapp.account.AccountRepository;
import org.springcorebankapp.account.AccountService;
import org.springcorebankapp.operations.ConsoleOperationType;
import org.springcorebankapp.operations.OperationCommandProcessor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Scanner;
@Component
public class AccountWithdrawProcessor implements OperationCommandProcessor {
    private final Scanner scanner;
    private AccountService accountService;

    public AccountWithdrawProcessor(Scanner scanner, AccountService accountService) {
        this.scanner = scanner;
        this.accountService = accountService;
    }


    @Override
    public void processOperation() {
        System.out.println("Enter account id:");
        int accountId = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter amount to withdraw:");

        int amountToWithdraw = Integer.parseInt(scanner.nextLine());
        accountService.withdrawFromAccount(accountId, amountToWithdraw);

        System.out.println("Successfully withdrawn amount=%s to account id=%s"
                .formatted(amountToWithdraw, accountId));
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_WITHDRAW;
    }
}
