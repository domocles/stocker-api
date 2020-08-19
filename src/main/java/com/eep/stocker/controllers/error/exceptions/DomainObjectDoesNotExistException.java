package com.eep.stocker.controllers.error.exceptions;

public class DomainObjectDoesNotExistException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DomainObjectDoesNotExistException(String message) { super(message); }
}
