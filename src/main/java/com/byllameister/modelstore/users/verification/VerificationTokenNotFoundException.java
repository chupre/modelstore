package com.byllameister.modelstore.users.verification;

public class VerificationTokenNotFoundException extends RuntimeException {
    public VerificationTokenNotFoundException() {
        super("Verification token not found");
    }
}
