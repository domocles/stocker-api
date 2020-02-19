package com.eep.stocker.controllers.error.exceptions;

public class DeliveryLineDoesNotExistException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DeliveryLineDoesNotExistException(String message) { super(message); }
}
