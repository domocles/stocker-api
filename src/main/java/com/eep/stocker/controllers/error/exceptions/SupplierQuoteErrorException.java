package com.eep.stocker.controllers.error.exceptions;

public class SupplierQuoteErrorException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SupplierQuoteErrorException(String message) {
        super(message);
    }
}
