package org.springcorebankapp.operations;

public interface OperationCommandProcessor {
    void processOperation();
    ConsoleOperationType getOperationType();
}
