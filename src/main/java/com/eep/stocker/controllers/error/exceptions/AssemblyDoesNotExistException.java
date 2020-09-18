package com.eep.stocker.controllers.error.exceptions;

public class AssemblyDoesNotExistException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AssemblyDoesNotExistException(String message) { super(message); }
}
