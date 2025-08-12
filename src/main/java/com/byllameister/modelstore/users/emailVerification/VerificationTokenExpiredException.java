package com.byllameister.modelstore.users.emailVerification;

public class VerificationTokenExpiredException extends RuntimeException {
    public VerificationTokenExpiredException() {
        super("Verification token has expired");
    }
}
