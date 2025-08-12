package com.byllameister.modelstore.auth;

public class TooManyAuthenticationRequestsException extends RuntimeException {
    public TooManyAuthenticationRequestsException() {
        super("Too many authentication requests");
    }
}
