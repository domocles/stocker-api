package com.eep.stocker.controllers.error.exceptions;

public class ReferenceGeneratorDoesNotExistException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ReferenceGeneratorDoesNotExistException(String message) { super(message);}
}
