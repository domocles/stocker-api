package com.eep.stocker.controllers.error.exceptions;

public class ReferenceGeneratorAlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ReferenceGeneratorAlreadyExistsException(String message) { super (message); }
}
