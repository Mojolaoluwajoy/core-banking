package com.corebanking.ledgerservice.exception;

public class GlAccountNotFoundException extends RuntimeException {
    public GlAccountNotFoundException(String message) {
        super(message);
    }
}
