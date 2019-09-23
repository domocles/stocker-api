package com.eep.stocker.controllers.error.exceptions;

public class StockableProductDoesNotExistException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public StockableProductDoesNotExistException(String message) {
        super(message);
    }
}
