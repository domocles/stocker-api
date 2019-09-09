package com.eep.stocker.controllers.error.exceptions;

public class ConstraintException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ConstraintException(String message) {
        super(message);
    }
}
