package com.eep.stocker.controllers.error.exceptions;

public class MpnNotUniqueException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public MpnNotUniqueException(String message) {
        super(message);
    }
}
