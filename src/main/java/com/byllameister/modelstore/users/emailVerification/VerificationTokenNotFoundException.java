package com.byllameister.modelstore.users.emailVerification;

public class VerificationTokenNotFoundException extends RuntimeException {
    public VerificationTokenNotFoundException() {
        super("Verification token not found");
    }
}
