package com.eep.stocker.controllers.error.exceptions;

public class StockTransactionDoesNotExistException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public StockTransactionDoesNotExistException(String message) {
        super(message);
    }
}
