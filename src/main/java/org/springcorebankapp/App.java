package org.thewhitemage13;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App
{

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("org.thewhitemage13");
        OperationConsoleListener consoleListener = context.getBean(OperationConsoleListener.class);
        consoleListener.start();
        consoleListener.listenUpdates();
        consoleListener.endListed();
    }
}
