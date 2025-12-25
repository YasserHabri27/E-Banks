package com.ebank.exception;

public class OperationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OperationException(String message) {
        super(message);
    }
}
