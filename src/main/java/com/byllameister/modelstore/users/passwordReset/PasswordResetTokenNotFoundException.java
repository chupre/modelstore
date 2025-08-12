package com.byllameister.modelstore.users.passwordReset;

public class PasswordResetTokenNotFoundException extends RuntimeException {
    public PasswordResetTokenNotFoundException() {
        super("Password reset token not found");
    }
}
