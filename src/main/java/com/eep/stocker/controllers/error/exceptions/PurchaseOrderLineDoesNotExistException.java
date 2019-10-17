package com.eep.stocker.controllers.error.exceptions;

public class PurchaseOrderLineDoesNotExistException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public PurchaseOrderLineDoesNotExistException(String message) {
        super(message);
    }
}
