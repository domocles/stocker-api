package com.eep.stocker.controllers.error.exceptions;

public class SupplierDoesNotExistException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public SupplierDoesNotExistException(String message) {
        super(message);
    }
}
