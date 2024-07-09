package org.springcorebankapp.operations.processors;

import org.springcorebankapp.account.AccountService;
import org.springcorebankapp.operations.ConsoleOperationType;
import org.springcorebankapp.operations.OperationCommandProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Scanner;
// auto Bean generate
@Component
public class AccountTransferProcessor implements OperationCommandProcessor {
    private final Scanner scanner;
    private final AccountService accountService;

    // @Autowired - если два или более конструктора, то используеи эту аннотацию
    // чтоб указать спрингу какой конструктор использовать для генерации бина
    public AccountTransferProcessor(AccountService accountService, Scanner scanner) {
        this.accountService = accountService;
        this.scanner = scanner;
    }

    @Override
    public void processOperation() {
        System.out.println("Enter source account id:");
        int fromAccountId = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter destination account id:");
        int toAccountId = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter amount to transfer:");
        int amountToTransfer = Integer.parseInt(scanner.nextLine());
        accountService.transfer(fromAccountId, toAccountId, amountToTransfer);
        System.out.println("Successfuly transferred %S from accountId %s to accountId %s"
                .formatted(amountToTransfer, fromAccountId, toAccountId)
        );
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_TRANSFER;
    }
}
