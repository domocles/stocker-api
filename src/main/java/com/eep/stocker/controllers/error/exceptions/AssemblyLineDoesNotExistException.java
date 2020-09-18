package com.eep.stocker.controllers.error.exceptions;

public class AssemblyLineDoesNotExistException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AssemblyLineDoesNotExistException(String message) {
        super(message);
    }
}
