package com.eep.stocker.controllers.error.exceptions;

public class SupplierQuoteDoesNotExistException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SupplierQuoteDoesNotExistException(String message) {
        super(message);
    }
}
