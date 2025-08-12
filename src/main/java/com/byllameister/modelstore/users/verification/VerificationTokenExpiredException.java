package com.byllameister.modelstore.users.verification;

public class VerificationTokenExpiredException extends RuntimeException {
    public VerificationTokenExpiredException() {
        super("Verification token has expired");
    }
}
