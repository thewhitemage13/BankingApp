package org.thewhitemage13;

import org.thewhitemage13.account.AccountService;
import org.thewhitemage13.operations.ConsoleOperationType;
import org.thewhitemage13.operations.OperationCommandProcessor;
import org.thewhitemage13.user.UserService;

import java.util.Map;
import java.util.Scanner;
//Клвсс для считавания операции, которую вводит пользователь
public class OperationConsoleListener {
    private Scanner scanner = new Scanner(System.in);
    private final Map<ConsoleOperationType, OperationCommandProcessor> processorMap;

    public OperationConsoleListener(Scanner scanner, Map<ConsoleOperationType, OperationCommandProcessor> processorMap) {
        this.scanner = scanner;
        this.processorMap = processorMap;
    }

    public void listenUpdates(){
        while(true){
            var operationType = listenNextOperation();
            processNextOperation(operationType);
        }
    }

    private ConsoleOperationType listenNextOperation() {
        while(true) {
            System.out.println("\nPlease type mext operation: ");
            printAllAvailableOperation();
            System.out.println();
            var nextOperation = scanner.nextLine();
            try {
                return ConsoleOperationType.valueOf(nextOperation);
            } catch (IllegalArgumentException e) {
                System.out.println("No such operation type");
            }
        }
    }

    private void printAllAvailableOperation() {
        processorMap.keySet()
                .forEach(System.out::println);
    }

    private void processNextOperation(ConsoleOperationType operationType){
        try{
            var processor = processorMap.get(operationType);
            processor.processOperation();
        } catch (Exception e){
            System.out.printf("Error execute command %s: error=%s%n", operationType, e.getMessage());
        }
    }

    public void start() {
        System.out.println("Console Listener started");
    }

    public void endListed() {
        System.out.println("Console listener end listen");
    }
}



























