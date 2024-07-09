package org.thewhitemage13;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thewhitemage13.account.AccountService;
import org.thewhitemage13.operations.ConsoleOperationType;
import org.thewhitemage13.operations.OperationCommandProcessor;
import org.thewhitemage13.user.UserService;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public Scanner scanner(){
        return new Scanner(System.in);
    }

    @Bean
    public OperationConsoleListener operationConsoleListener(Scanner scanner,
                                                             List<OperationCommandProcessor> commandProcessorList){
        Map<ConsoleOperationType, OperationCommandProcessor> map =
                commandProcessorList
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        OperationCommandProcessor::getOperationType,
                                processor -> processor
                        )
                );
        return new OperationConsoleListener(scanner, map);
    }

    @Bean
    public UserService userService(AccountService accountService){
        return new UserService(accountService);
    }

    @Bean
    public AccountService accountService(){
        return new AccountService();
    }
}
