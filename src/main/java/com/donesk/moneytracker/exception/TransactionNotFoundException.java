package com.donesk.moneytracker.exception;

public class TransactionNotFoundException extends RuntimeException {

    public TransactionNotFoundException(String msg) {
        super(msg);
    }

    public TransactionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
