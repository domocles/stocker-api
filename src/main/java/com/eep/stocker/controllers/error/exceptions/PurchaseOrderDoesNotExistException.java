package com.eep.stocker.controllers.error.exceptions;

public class PurchaseOrderDoesNotExistException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public PurchaseOrderDoesNotExistException(String message) {
        super(message);
    }
}
