package com.eep.stocker.controllers.error.exceptions;

public class MpnNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public MpnNotFoundException(String message) { super(message); }
}
