package com.eep.stocker.controllers.error.exceptions;

public class DeliveryDoesNotExistException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DeliveryDoesNotExistException(String message) { super(message); };
}
