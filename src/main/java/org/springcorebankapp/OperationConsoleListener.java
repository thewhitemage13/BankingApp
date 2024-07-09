package org.springcorebankapp;

import org.springcorebankapp.operations.ConsoleOperationType;
import org.springcorebankapp.operations.OperationCommandProcessor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

//Клвсс для считавания операции, которую вводит пользователь
@Component
public class OperationConsoleListener {

    private Scanner scanner = new Scanner(System.in);
    private final Map<ConsoleOperationType, OperationCommandProcessor> processorMap;

    public OperationConsoleListener(Scanner scanner,
                                    List<OperationCommandProcessor> operationList) {
        this.scanner = scanner;
        this.processorMap = operationList
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        OperationCommandProcessor::getOperationType,
                                        processor -> processor
                                )
                        );
    }

    public void listenUpdates(){
        while(!Thread.currentThread().isInterrupted()){
            var operationType = listenNextOperation();
            if(operationType == null){
                return;
            }
            processNextOperation(operationType);
        }
    }

    private ConsoleOperationType listenNextOperation() {
        while(!Thread.currentThread().isInterrupted()) {
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
        return null;
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

    public void endListen() {
        System.out.println("Console listener end listen");
    }
}
