package com.eep.stocker.controllers.error.exceptions;

public class DomainObjectAlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DomainObjectAlreadyExistsException(String message) {
        super(message);
    }
}
