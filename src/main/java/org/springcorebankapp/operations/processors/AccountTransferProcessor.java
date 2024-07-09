package org.thewhitemage13.operations.processors;

import org.thewhitemage13.operations.ConsoleOperationType;
import org.thewhitemage13.operations.OperationCommandProcessor;

public class AccountTransferProcessor implements OperationCommandProcessor {
    @Override
    public void processOperation() {

    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_TRANSFER;
    }
}
